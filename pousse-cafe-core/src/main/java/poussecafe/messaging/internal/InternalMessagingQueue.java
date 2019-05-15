package poussecafe.messaging.internal;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import poussecafe.messaging.MessageAdapter;
import poussecafe.messaging.MessageReceiver;
import poussecafe.messaging.MessageSender;
import poussecafe.runtime.MessageConsumer;
import poussecafe.runtime.OriginalAndMarshaledMessage;

public class InternalMessagingQueue {

    InternalMessagingQueue(MessageConsumer messageConsumer) {
        messageReceiver = new InternalMessageReceiver(messageAdapter, messageConsumer);
        messageSender = new InternalMessageSender(messageAdapter);
    }

    private MessageAdapter messageAdapter = new SerializingMessageAdapter();

    public class InternalMessageReceiver extends MessageReceiver {

        private InternalMessageReceiver(MessageAdapter messageAdapter, MessageConsumer messageConsumer) {
            super(messageAdapter, messageConsumer);
        }

        @Override
        protected void actuallyStartReceiving() {
            receptionThread = new Thread(() -> {
                while (true) {
                    try {
                        Object takenObject = queue.take();
                        mutex.acquire();
                        if (STOP.equals(takenObject)) {
                            return;
                        } else {
                            onMessage(takenObject);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        interrupted = true;
                        return;
                    } finally {
                        // Catch-all, thread must continue to run until explicitly stopped
                        mutex.release();
                    }
                }
            });
            receptionThread.setDaemon(true);
            receptionThread.setName("internal message queue");
            receptionThread.start();
        }

        private Thread receptionThread;

        private boolean interrupted;

        @Override
        protected void actuallyStopReceiving() {
            queue.add(STOP);
        }

        private static final String STOP = "stop";

        public InternalMessagingQueue queue() {
            return InternalMessagingQueue.this;
        }

        @Override
        protected void actuallyInterruptReception() {
            receptionThread.interrupt();
            mutex.release();
        }

        public boolean isInterrupted() {
            return interrupted;
        }
    }

    private Semaphore mutex = new Semaphore(1);

    private BlockingQueue<Object> queue = new LinkedBlockingQueue<>();

    private InternalMessageReceiver messageReceiver;

    public MessageReceiver messageReceiver() {
        return messageReceiver;
    }

    public class InternalMessageSender extends MessageSender {

        private InternalMessageSender(MessageAdapter messageAdapter) {
            super(messageAdapter);
        }

        @Override
        protected void sendMarshalledMessage(OriginalAndMarshaledMessage marshalledMessage) {
            queue.add(marshalledMessage.marshaled());
        }
    }

    private InternalMessageSender messageSender;

    public MessageSender messageSender() {
        return messageSender;
    }

    public void waitUntilEmptyOrInterrupted()
            throws InterruptedException {
        boolean messagesQueued;
        do {
            mutex.acquire();
            if(messageReceiver.isInterrupted()) {
                mutex.release();
                return;
            }
            messagesQueued = !queue.isEmpty();
            mutex.release();
        } while (messagesQueued);
    }
}
