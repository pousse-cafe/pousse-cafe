package poussecafe.storage;

import java.util.Objects;
import poussecafe.messaging.Message;
import poussecafe.runtime.MessageSenderLocator;

public abstract class MessageSendingPolicy {

    private MessageSenderLocator messageSenderLocator;

    public abstract void considerSending(MessageCollection collection);

    public abstract MessageCollection newMessageCollection();

    protected void sendCollection(MessageCollection collection) {
        for (Message message : collection.getMessages()) {
            messageSenderLocator.locate(message.getClass()).sendMessage(message);
        }
    }

    public void setMessageSenderLocator(MessageSenderLocator messageSenderLocator) {
        Objects.requireNonNull(messageSenderLocator);
        this.messageSenderLocator = messageSenderLocator;
    }

}
