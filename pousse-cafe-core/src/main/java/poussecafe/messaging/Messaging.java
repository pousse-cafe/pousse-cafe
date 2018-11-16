package poussecafe.messaging;

import poussecafe.context.MessageConsumer;

import static poussecafe.check.Checks.checkThatValue;

public abstract class Messaging {

    public void configure(MessageConsumer messageConsumer) {
        checkThatValue(messageConsumer).notNull();
        messageSender = buildMessageSender();
        messageReceiver = buildMessageReceiver(messageConsumer);
    }

    protected abstract MessageSender buildMessageSender();

    protected abstract MessageReceiver buildMessageReceiver(MessageConsumer messageConsumer);

    private MessageSender messageSender;

    private MessageReceiver messageReceiver;

    public MessageSender messageSender() {
        return messageSender;
    }

    public MessageReceiver messageReceiver() {
        return messageReceiver;
    }

    public MessagingUnitBuilder newMessagingUnit() {
        return new MessagingUnitBuilder(this);
    }

    public abstract String name();

    public boolean nameIn(String[] array) {
        for(String name : array) {
            if(name().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
