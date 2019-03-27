package poussecafe.shop.domain;

import poussecafe.discovery.MessageListener;
import poussecafe.domain.Factory;

public class OrderFactory extends Factory<OrderKey, Order, Order.Attributes> {

    /**
     * @process OrderPlacement
     */
    @MessageListener
    public Order buildPlacedOrder(OrderPlaced event) {
        OrderDescription description = event.description().value();
        OrderKey key = new OrderKey(event.productKey().value(), description.customerKey(), description.reference());
        Order order = newAggregateWithKey(key);
        order.attributes().units().value(description.units());
        return order;
    }
}
