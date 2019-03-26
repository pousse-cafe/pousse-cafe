package poussecafe.shop.domain;

import java.util.Objects;
import poussecafe.discovery.Aggregate;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate(
  factory = MessageFactory.class,
  repository = MessageRepository.class
)
public class Message extends AggregateRoot<MessageKey, Message.Attributes> {

    void setCustomerKey(CustomerKey customerKey) {
        Objects.requireNonNull(customerKey);
        attributes().setCustomerKey(customerKey);
    }

    public void setContentType(ContentType type) {
        Objects.requireNonNull(type);
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
