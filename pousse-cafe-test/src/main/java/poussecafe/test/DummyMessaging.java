package poussecafe.test;

import java.util.Objects;
import poussecafe.messaging.MessageReceiverConfiguration;
import poussecafe.messaging.Messaging;
import poussecafe.messaging.MessagingConnection;

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
    public MessagingConnection connect(MessageReceiverConfiguration configuration) {
        return new MessagingConnection.Builder()
                .messaging(this)
                .messageReceiver(new DummyMessageReceiver(configuration))
                .messageSender(new DummyMessageSender())
                .build();
    }
}
