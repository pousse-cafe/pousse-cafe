package poussecafe.messaging.internal;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageAdapter;
import poussecafe.messaging.MessageReceiver;
import poussecafe.messaging.MessageSender;
import poussecafe.processing.MessageBroker;
import poussecafe.processing.ReceivedMessage;
import poussecafe.runtime.OriginalAndMarshaledMessage;

public class InternalMessagingQueue {

    InternalMessagingQueue(MessageBroker messageBroker) {
        messageReceiver = new InternalMessageReceiver(messageBroker);
        messageSender = new InternalMessageSender(messageAdapter);
    }

    private MessageAdapter messageAdapter = new SerializingMessageAdapter();

    public class InternalMessageReceiver extends MessageReceiver {

        private InternalMessageReceiver(MessageBroker messageBroker) {
            super(messageBroker);
            messagesBeingProcessed = new AtomicReference<>(0);
        }

        @Override
        protected void actuallyStartReceiving() {
            receptionThread = new Thread(() -> {
                while (true) {
                    try {
                        available.acquire();
                        mutex.acquire();
                        Object polledObject = queue.poll();
                        if (STOP.equals(polledObject)) {
                            return;
                        } else {
                            messagesBeingProcessed.updateAndGet(value -> value + 1);
                            Message message = messageAdapter.adaptSerializedMessage(polledObject);
                            onMessage(new ReceivedMessage.Builder()
                                    .payload(new OriginalAndMarshaledMessage.Builder()
                                            .marshaled(polledObject)
                                            .original(message)
                                            .build())
                                    .acker(() -> messagesBeingProcessed.updateAndGet(value -> value - 1))
                                    .build());
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

        private AtomicReference<Integer> messagesBeingProcessed;

        @Override
        protected void actuallyStopReceiving() {
            queue.add(STOP);
            available.release();
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

    private Semaphore available = new Semaphore(0);

    private Semaphore mutex = new Semaphore(1);

    private Queue<Object> queue = new LinkedBlockingQueue<>();

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
            available.release();
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
            messagesQueued = !queue.isEmpty() || messageReceiver.messagesBeingProcessed.get() > 0;
            mutex.release();
        } while (messagesQueued);
    }
}
