package poussecafe.sample.domain;

import poussecafe.context.Aggregate;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityData;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

@Aggregate(
  factory = MessageFactory.class,
  repository = MessageRepository.class
)
public class Message extends AggregateRoot<MessageKey, Message.Data> {

    void setCustomerKey(CustomerKey customerKey) {
        checkThat(value(customerKey).notNull().because("Customer key cannot be null"));
        data().setCustomerKey(customerKey);
    }

    public void setContentType(ContentType type) {
        checkThat(value(type).notNull().because("Content type cannot be null"));
        data().setContentType(type);
    }

    public ContentType getContentType() {
        return data().getContentType();
    }

    @Override
    public void onAdd() {
        MessageCreated event = newDomainEvent(MessageCreated.class);
        event.messageKey().set(getKey());
        addDomainEvent(event);
    }

    public static interface Data extends EntityData<MessageKey> {

        void setCustomerKey(CustomerKey customerKey);

        CustomerKey getCustomerKey();

        void setContentType(ContentType type);

        ContentType getContentType();
    }

}
