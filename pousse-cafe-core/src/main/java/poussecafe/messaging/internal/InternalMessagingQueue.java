package poussecafe.messaging.internal;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import poussecafe.exception.RuntimeInterruptedException;
import poussecafe.messaging.EnvelopeSource;
import poussecafe.messaging.MessageAdapter;
import poussecafe.messaging.MessageReceiver;
import poussecafe.messaging.MessageReceiverConfiguration;
import poussecafe.messaging.MessageSender;
import poussecafe.messaging.ReceptionThreadMessageReceiver;
import poussecafe.runtime.OriginalAndMarshaledMessage;

public class InternalMessagingQueue {

    InternalMessagingQueue(MessageReceiverConfiguration configuration) {
        messageReceiver = new InternalMessageReceiver(configuration);
        messageSender = new InternalMessageSender(messageAdapter);
    }

    private MessageAdapter messageAdapter = new SerializingMessageAdapter();

    public class InternalMessageReceiver extends ReceptionThreadMessageReceiver<Object> {

        private InternalMessageReceiver(MessageReceiverConfiguration configuration) {
            super(configuration);
        }

        @Override
        protected EnvelopeSource<Object> envelopeSource() {
            return new EnvelopeSource<>() {
                @Override
                public Object get() {
                    try {
                        return queue.take();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        state.interrupt();
                        throw new RuntimeInterruptedException(e);
                    }
                }

                @Override
                public void close() throws IOException {
                    queue.add(STOP);
                }
            };
        }

        private static final String STOP = "stop";

        @Override
        protected void onMessage(Object envelope) {
            if (!STOP.equals(envelope)) {
                state.addMessageBeingProcessing();
                state.removeQueuedMessage();
                super.onMessage(envelope);
            }
        }

        @Override
        protected Object extractPayload(Object envelope) {
            return envelope;
        }

        @Override
        protected poussecafe.messaging.Message deserialize(Object payload) {
            return messageAdapter.adaptSerializedMessage(payload);
        }

        @Override
        protected Runnable buildAcker(Object envelope) {
            return state::removeMessageBeingProcessing;
        }

        public InternalMessagingQueue queue() {
            return InternalMessagingQueue.this;
        }

        public boolean isInterrupted() {
            return state.interrupted();
        }
    }

    private QueueState state = new QueueState();

    private BlockingQueue<Object> queue = new LinkedBlockingQueue<>();

    private InternalMessageReceiver messageReceiver;

    public MessageReceiver<Object> messageReceiver() {
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
