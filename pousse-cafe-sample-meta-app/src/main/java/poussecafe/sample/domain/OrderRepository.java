package poussecafe.sample.domain;

import poussecafe.domain.Repository;
import poussecafe.property.MessageCollection;
import poussecafe.sample.domain.Order.Data;

public class OrderRepository extends Repository<Order, OrderKey, Data> {

    @Override
    protected void considerMessageSendingAfterAdd(Order order,
            MessageCollection messageCollection) {
        OrderCreated event = newComponent(OrderCreated.class);
        event.orderKey().set(order.getKey());
        messageCollection.addMessage(event);
        super.considerMessageSendingAfterAdd(order, messageCollection);
    }
}
