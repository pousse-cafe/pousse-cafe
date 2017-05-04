package poussecafe.sample.domain;

import poussecafe.domain.Repository;
import poussecafe.sample.domain.Order.Data;
import poussecafe.storable.MessageCollection;

public class OrderRepository extends Repository<Order, OrderKey, Data> {

    @Override
    protected Order newAggregate() {
        return new Order();
    }

    @Override
    protected void considerMessageSendingAfterAdd(Order order,
            MessageCollection messageCollection) {
        messageCollection.addMessage(new OrderCreated(order.getKey()));
        super.considerMessageSendingAfterAdd(order, messageCollection);
    }
}
