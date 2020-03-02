package poussecafe.processing;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.exception.PousseCafeException;

class MessageProcessingThread {

    public static class Builder {

        private MessageProcessingThread thread = new MessageProcessingThread();

        public Builder messageProcessor(MessageProcessor messageProcessor) {
            thread.messageProcessor = messageProcessor;
            return this;
        }

        public Builder threadId(int threadId) {
            thread.threadId = threadId;
            return this;
        }

        public MessageProcessingThread build() {
            Objects.requireNonNull(thread.messageProcessor);
            if(thread.threadId == -1) {
                throw new IllegalStateException();
            }
            return thread;
        }
    }

    private MessageProcessingThread() {

    }

    public void submit(MessageToProcess message) {
        try {
            workQueue.put(new UnitOfWork(message));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PousseCafeException("Unable to submit message, current thread was interrupted");
        }
    }

    private static class UnitOfWork {

        private UnitOfWork() {

        }

        private UnitOfWork(MessageToProcess message) {
            Objects.requireNonNull(message);
            this.message = message;
        }

        MessageToProcess message;
    }

    private static final UnitOfWork STOP = new UnitOfWork();

    private BlockingQueue<UnitOfWork> workQueue = new LinkedBlockingQueue<>();

    public synchronized void start() {
        if(underlyingThread != null) {
            throw new IllegalStateException("Processing thread was already started");
        }
        underlyingThread = new Thread(this::processor);
        underlyingThread.setName("Processing Thread " + threadId);
        underlyingThread.setDaemon(true);
        underlyingThread.start();
    }

    private Thread underlyingThread;

    private void processor() {
        logger.info("Starting processing thread {}...", threadId);
        while(true) {
            try {
                UnitOfWork unitOfWork = workQueue.take();
                if(unitOfWork != STOP) {
                    processAndSignal(unitOfWork);
                } else {
                    break;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new PousseCafeException("Unable to process message, current thread was interrupted");
            } catch (Exception e) {
                logger.error("Unhandled exception in processing thread", e);
            }
        }
        logger.info("Processing thread {} stops.", threadId);
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    private void processAndSignal(UnitOfWork unitOfWork) {
        try {
            messageProcessor.processMessage(unitOfWork.message.receivedMessagePayload());
        } catch (Exception e) {
            logger.error("Unhandled exception while processing unit of work", e);
        } finally {
            unitOfWork.message.signalProcessed(threadId);
        }
    }

    private MessageProcessor messageProcessor;

    private int threadId = -1;

    public void stopAndWait(Duration timeOut) {
        stop();
        join(timeOut);
    }

    public void join() {
        join(Duration.ofMillis(0));
    }

    public void join(Duration timeOut) {
        try {
            underlyingThread.join(timeOut.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PousseCafeException("Thread was interrupted while waiting for underlying thread to stop");
        }
    }

    public synchronized void stop() {
        try {
            workQueue.put(STOP);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PousseCafeException("Thread was interrupted while stopping processing");
        }
    }
}
