package poussecafe.messaging.internal;

import poussecafe.messaging.Messaging;
import poussecafe.messaging.MessagingConnection;
import poussecafe.processing.MessageBroker;

public class InternalMessaging extends Messaging {

    public static final String NAME = "internal";

    @Override
    public String name() {
        return NAME;
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

    private InternalMessaging() {

    }

    @Override
    public MessagingConnection connect(MessageBroker messageBroker) {
        InternalMessagingQueue queue = new InternalMessagingQueue(messageBroker);
        return new MessagingConnection.Builder()
                .messaging(this)
                .messageSender(queue.messageSender())
                .messageReceiver(queue.messageReceiver())
                .build();
    }
}
