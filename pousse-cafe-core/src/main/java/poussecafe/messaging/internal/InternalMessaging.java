package poussecafe.messaging.internal;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import poussecafe.context.MessageConsumer;
import poussecafe.messaging.MessageAdapter;
import poussecafe.messaging.MessageReceiver;
import poussecafe.messaging.MessageSender;
import poussecafe.messaging.Messaging;

public class InternalMessaging extends Messaging {

    public static final String NAME = "internal";

    @Override
    public String name() {
        return NAME;
    }

    private InternalMessaging() {

    }

    public static InternalMessaging instance() {
        synchronized(InternalMessaging.class) {
            if(instance == null) {
                instance = new InternalMessaging();
            }
            return instance;
        }
    }

    private static InternalMessaging instance;

    public static void tearDown() {
        synchronized(InternalMessaging.class) {
            instance = null;
        }
    }

    private MessageAdapter messageAdapter = new SerializingMessageAdapter();

    private Queue<Object> queue = new LinkedBlockingQueue<>();

    private Semaphore available = new Semaphore(0);

    private Semaphore mutex = new Semaphore(1);

    public class InternalMessageSender extends MessageSender {

        public InternalMessageSender(MessageAdapter messageAdapter) {
            super(messageAdapter);
        }

        @Override
        protected void sendMarshalledMessage(Object marshalledMessage) {
            queue.add(marshalledMessage);
            available.release();
        }
    }

    public class InternalMessageReceiver extends MessageReceiver {

        public InternalMessageReceiver(MessageAdapter messageAdapter, MessageConsumer messageConsumer) {
            super(messageAdapter, messageConsumer);
        }

        @Override
        protected void actuallyStartReceiving() {
            Thread t = new Thread(() -> {
                try {
                    while (true) {
                        available.acquire();
                        mutex.acquire();
                        onMessage(queue.poll());
                        mutex.release();
                    }
                } catch (InterruptedException e) {
                    return;
                }
            });
            t.setDaemon(true);
            t.setName("internal message queue");
            t.start();
        }
    }

    public void waitUntilEmpty()
            throws InterruptedException {
        boolean messagesQueued;
        do {
            mutex.acquire();
            messagesQueued = !queue.isEmpty();
            mutex.release();
        } while (messagesQueued);
    }

    @Override
    protected MessageSender buildMessageSender() {
        return new InternalMessageSender(messageAdapter);
    }

    @Override
    protected MessageReceiver buildMessageReceiver(MessageConsumer messageConsumer) {
        return new InternalMessageReceiver(messageAdapter, messageConsumer);
    }
}
