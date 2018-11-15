package poussecafe.sample.domain;

import poussecafe.domain.DomainEvent;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class MessageCreated implements DomainEvent {

    private MessageKey messageKey;

    public MessageCreated(MessageKey key) {
        setMessageKey(key);
    }

    public MessageKey getMessageKey() {
        return messageKey;
    }

    private void setMessageKey(MessageKey messageKey) {
        checkThat(value(messageKey).notNull().because("Message key cannot be null"));
        this.messageKey = messageKey;
    }

}
