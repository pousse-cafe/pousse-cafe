package poussecafe.storage;

import poussecafe.messaging.Message;
import poussecafe.messaging.MessageSender;
import poussecafe.property.MessageCollection;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public abstract class MessageSendingPolicy {

    private MessageSender messageSender;

    public abstract void considerSending(MessageCollection collection);

    public abstract MessageCollection newMessageCollection();

    protected void sendCollection(MessageCollection collection) {
        for (Message message : collection.getMessages()) {
            messageSender.sendMessage(message);
        }
    }

    public void setMessageSender(MessageSender messageSender) {
        checkThat(value(messageSender).notNull().because("Message sender cannot be null"));
        this.messageSender = messageSender;
    }

}
