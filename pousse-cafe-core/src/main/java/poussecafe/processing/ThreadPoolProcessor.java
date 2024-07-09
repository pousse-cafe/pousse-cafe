package poussecafe.processing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import poussecafe.processing.MessageToProcess.Callback;

public class ThreadPoolProcessor implements Processor {

    public static class Builder {

        private ThreadPoolProcessor pool = new ThreadPoolProcessor();

        public Builder poolSize(int poolSize) {
            this.poolSize = poolSize;
            return this;
        }

        private int poolSize;

        public Builder messageConsumptionConfiguration(MessageConsumptionConfiguration messageConsumptionConfiguration) {
            this.messageConsumptionConfiguration = messageConsumptionConfiguration;
            return this;
        }

        private MessageConsumptionConfiguration messageConsumptionConfiguration;

        public ThreadPoolProcessor build() {
            if(poolSize <= 0) {
                throw new IllegalStateException("Message processing thread pool size must be greater than zero");
            }
            pool.messageProcessingThreads = new MessageProcessingThread[poolSize];
            createThreads();

            pool.processingThreadSelector =
                    new DefaultProcessingThreadSelector(poolSize);

            return pool;
        }

        private void createThreads() {
            for(int i = 0; i < pool.messageProcessingThreads.length; ++i) {
                MessageProcessor processor = new MessageProcessor.Builder()
                        .id(Integer.toString(i))
                        .messageConsumptionConfiguration(messageConsumptionConfiguration)
                        .build();
                MessageProcessingThread thread = new MessageProcessingThread.Builder()
                        .threadId(i)
                        .messageProcessor(processor)
                        .build();
                pool.messageProcessingThreads[i] = thread;
            }
        }
    }

    private ThreadPoolProcessor() {

    }

    private MessageProcessingThread[] messageProcessingThreads;

    @Override
    public void start() {
        for(int i = 0; i < messageProcessingThreads.length; ++i) {
            messageProcessingThreads[i].start();
        }
    }

    @Override
    public void stop() {
        for(int i = 0; i < messageProcessingThreads.length; ++i) {
            messageProcessingThreads[i].stop();
        }
    }

    @Override
    public void submit(ReceivedMessage receivedMessage, List<MessageListenersGroup> groups) {
        var receivedMessageProcessingState = new ReceivedMessageProcessingState.Builder()
                .receivedMessage(receivedMessage)
                .firstId(nextReceivedMessageId)
                .sequenceLength(groups.size())
                .build();

        for(MessageListenersGroup group : groups) {
            var receivedMessageId = nextReceivedMessageId++;
            MessageToProcess messageToProcess = new MessageToProcess.Builder()
                    .receivedMessageId(receivedMessageId)
                    .messageListenerGroup(group)
                    .callback(callback)
                    .build();

            int threadId = processingThreadSelector.selectFor(group);
            if(threadId < 0 || threadId >= messageProcessingThreads.length) {
                throw new IllegalArgumentException("Thread ID must be between 0 and (pool size - 1) inclusive");
            }
            inProgressProcessingStates.put(messageToProcess.receivedMessageId(), receivedMessageProcessingState);
            MessageProcessingThread thread = messageProcessingThreads[threadId];
            thread.submit(messageToProcess);
        }
    }

    private long nextReceivedMessageId = 0;

    private MessageBrokerCallback callback = new MessageBrokerCallback();

    private ProcessingThreadSelector processingThreadSelector;

    private synchronized void signalProcessed(int threadId, MessageToProcess processedMessage) { // NOSONAR - synchronized
        long receivedMessageId = processedMessage.receivedMessageId();
        var processingProgress = inProgressProcessingStates.remove(receivedMessageId);
        if(processingProgress == null) {
            throw new IllegalArgumentException("No processing state available");
        } else {
            processingThreadSelector.unselect(threadId, processedMessage.messageListenerGroup());
            processingProgress.ackReceivedMessageId(processedMessage.receivedMessageId());
            if(processingProgress.isCompleted()) {
                logger.debug("Processing of message {} completed, acking...", receivedMessageId);
                processingProgress.receivedMessage().ack();
            }
        }
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Map<Long, ReceivedMessageProcessingState> inProgressProcessingStates = new HashMap<>();

    private class MessageBrokerCallback implements Callback {

        @Override
        public void signalProcessed(int threadId, MessageToProcess processedMessage) {
            ThreadPoolProcessor.this.signalProcessed(threadId, processedMessage);
        }
    }
}
