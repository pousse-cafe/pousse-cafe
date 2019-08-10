package poussecafe.test;

import java.util.Objects;
import poussecafe.messaging.Messaging;
import poussecafe.messaging.MessagingConnection;
import poussecafe.processing.MessageBroker;

public class DummyMessaging extends Messaging {

    public DummyMessaging(String name) {
        Objects.requireNonNull(name);
        this.name = name;
    }

    private String name;

    @Override
    public String name() {
        return name;
    }

    @Override
    public MessagingConnection connect(MessageBroker messageBroker) {
        return new MessagingConnection.Builder()
                .messaging(this)
                .messageReceiver(new DummyMessageReceiver(messageBroker))
                .messageSender(new DummyMessageSender())
                .build();
    }
}
