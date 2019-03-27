package poussecafe.shop.domain;

import poussecafe.attribute.Attribute;
import poussecafe.discovery.Aggregate;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate(
  factory = MessageFactory.class,
  repository = MessageRepository.class
)
public class Message extends AggregateRoot<MessageKey, Message.Attributes> {

    /**
     * @process Messaging
     * @event MessageCreated
     */
    @Override
    public void onAdd() {
        MessageCreated event = newDomainEvent(MessageCreated.class);
        event.messageKey().valueOf(attributes().key());
        emitDomainEvent(event);
    }

    public static interface Attributes extends EntityAttributes<MessageKey> {

        Attribute<CustomerKey> customerKey();

        Attribute<ContentType> contentType();
    }

}
