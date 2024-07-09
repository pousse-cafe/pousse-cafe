package poussecafe.messaging.sync;

import poussecafe.messaging.MessageReceiverConfiguration;
import poussecafe.messaging.MessageSender;
import poussecafe.messaging.Messaging;
import poussecafe.messaging.MessagingConnection;
import poussecafe.messaging.internal.SerializingMessageAdapter;
import poussecafe.runtime.OriginalAndMarshaledMessage;

public class SynchronousMessaging extends Messaging {

    public static final String NAME = "sync";

    @Override
    public String name() {
        return NAME;
    }

    public static SynchronousMessaging instance() {
        synchronized(SynchronousMessaging.class) {
            if(instance == null) {
                instance = new SynchronousMessaging();
            }
            return instance;
        }
    }

    private static SynchronousMessaging instance;

    private SynchronousMessaging() {

    }

    @Override
    public MessagingConnection connect(MessageReceiverConfiguration configuration) {
        var messageReceiver = new SynchronousMessageReceiver(configuration);
        var messageSender = new MessageSender(new SerializingMessageAdapter()) {
            @Override
            protected void sendMarshalledMessage(OriginalAndMarshaledMessage marshalledMessage) {
                messageReceiver.processMessage(marshalledMessage.marshaled());
            }
        };
        return new MessagingConnection.Builder()
                .messaging(this)
                .messageSender(messageSender)
                .messageReceiver(messageReceiver)
                .build();
    }
}
