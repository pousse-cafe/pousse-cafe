package poussecafe.shop.domain;

import java.util.UUID;
import poussecafe.discovery.MessageListener;
import poussecafe.domain.Factory;

public class MessageFactory extends Factory<MessageId, Message, Message.Attributes> {

    /**
     * @process Messaging
     */
    @MessageListener
    public Message buildMessage(OrderRejected event) {
        Message message = buildMessage(event.description().value().customerId());
        message.attributes().contentType().value(ContentType.ORDER_REJECTED);
        return message;
    }

    private Message buildMessage(CustomerId customerId) {
        Message message = newAggregateWithId(new MessageId(UUID.randomUUID().toString()));
        message.attributes().customerId().value(customerId);
        return message;
    }

    /**
     * @process Messaging
     */
    @MessageListener
    public Message buildMessage(OrderCreated event) {
        Message message = buildMessage(event.orderId().value().getCustomerId());
        message.attributes().contentType().value(ContentType.ORDER_READY_FOR_SETTLEMENT);
        return message;
    }

    /**
     * @process Messaging
     */
    @MessageListener
    public Message buildMessage(OrderSettled event) {
        Message message = buildMessage(event.orderId().value().getCustomerId());
        message.attributes().contentType().value(ContentType.ORDER_SETTLED);
        return message;
    }

    /**
     * @process Messaging
     */
    @MessageListener
    public Message buildMessage(OrderReadyForShipping event) {
        Message message = buildMessage(event.orderId().value().getCustomerId());
        message.attributes().contentType().value(ContentType.ORDER_READY_FOR_SHIPMENT);
        return message;
    }
}
