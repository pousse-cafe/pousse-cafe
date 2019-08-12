package poussecafe.messaging.internal;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
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
                        Object polledObject = queue.take();
                        if (STOP.equals(polledObject)) {
                            return;
                        } else {
                            messagesBeingProcessed.updateAndGet(value -> value + 1);
                            queuedMessages.updateAndGet(value -> value - 1);
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
        }

        private static final String STOP = "stop";

        public InternalMessagingQueue queue() {
            return InternalMessagingQueue.this;
        }

        @Override
        protected void actuallyInterruptReception() {
            receptionThread.interrupt();
        }

        public boolean isInterrupted() {
            return interrupted;
        }
    }

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
            queuedMessages.updateAndGet(value -> value + 1);
            try {
                queue.put(marshalledMessage.marshaled());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException();
            }
        }
    }

    private InternalMessageSender messageSender;

    private AtomicReference<Integer> queuedMessages = new AtomicReference<>(0);

    public MessageSender messageSender() {
        return messageSender;
    }

    public void waitUntilEmptyOrInterrupted() {
        boolean messagesQueued;
        do {
            messagesQueued = queuedMessages.get() > 0 || messageReceiver.messagesBeingProcessed.get() > 0;
        } while (messagesQueued);
    }
}
