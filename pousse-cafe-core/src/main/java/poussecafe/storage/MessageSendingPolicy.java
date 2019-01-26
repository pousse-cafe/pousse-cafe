package poussecafe.storage;

import poussecafe.context.MessageSenderLocator;
import poussecafe.domain.MessageCollection;
import poussecafe.messaging.Message;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

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
        checkThat(value(messageSenderLocator).notNull().because("Message sender locator cannot be null"));
        this.messageSenderLocator = messageSenderLocator;
    }

}
