package poussecafe.sample.domain;

import poussecafe.discovery.Aggregate;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

@Aggregate(
  factory = MessageFactory.class,
  repository = MessageRepository.class
)
public class Message extends AggregateRoot<MessageKey, Message.Attributes> {

    void setCustomerKey(CustomerKey customerKey) {
        checkThat(value(customerKey).notNull().because("Customer key cannot be null"));
        attributes().setCustomerKey(customerKey);
    }

    public void setContentType(ContentType type) {
        checkThat(value(type).notNull().because("Content type cannot be null"));
        attributes().setContentType(type);
    }

    public ContentType getContentType() {
        return attributes().getContentType();
    }

    @Override
    public void onAdd() {
        MessageCreated event = newDomainEvent(MessageCreated.class);
        event.messageKey().valueOf(attributes().key());
        emitDomainEvent(event);
    }

    public static interface Attributes extends EntityAttributes<MessageKey> {

        void setCustomerKey(CustomerKey customerKey);

        CustomerKey getCustomerKey();

        void setContentType(ContentType type);

        ContentType getContentType();
    }

}
