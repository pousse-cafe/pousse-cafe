package poussecafe.messaging.internal;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
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
        }

        @Override
        protected void actuallyStartReceiving() {
            receptionThread = new Thread(() -> {
                while (true) {
                    try {
                        Object polledObject = queue.take();
                        if (!STOP.equals(polledObject)) {
                            state.addMessageBeingProcessing();
                            state.removeQueuedMessage();
                            Message message = messageAdapter.adaptSerializedMessage(polledObject);
                            onMessage(new ReceivedMessage.Builder()
                                    .payload(new OriginalAndMarshaledMessage.Builder()
                                            .marshaled(polledObject)
                                            .original(message)
                                            .build())
                                    .acker(() -> state.removeMessageBeingProcessing())
                                    .interrupter(this::interruptReception)
                                    .build());
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        state.interrupt();
                        break;
                    }
                }
            });
            receptionThread.setDaemon(true);
            receptionThread.setName("Internal Messaging Reception Thread");
            receptionThread.start();
        }

        private Thread receptionThread;

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
            return state.interrupted();
        }
    }

    private QueueState state = new QueueState();

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
            state.addQueuedMessage();
            try {
                queue.put(marshalledMessage.marshaled());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException();
            }
        }
    }

    private InternalMessageSender messageSender;

    public MessageSender messageSender() {
        return messageSender;
    }

    public void waitUntilEmptyOrInterrupted() {
        while(!state.emptyOrInterrupted()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
