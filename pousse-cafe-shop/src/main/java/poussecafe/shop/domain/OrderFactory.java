package poussecafe.shop.domain;

import poussecafe.discovery.MessageListener;
import poussecafe.domain.Factory;

public class OrderFactory extends Factory<OrderId, Order, Order.Attributes> {

    /**
     * @process OrderPlacement
     */
    @MessageListener
    public Order buildPlacedOrder(OrderPlaced event) {
        OrderDescription description = event.description().value();
        OrderId id = new OrderId(event.productId().value(), description.customerId(), description.reference());
        Order order = newAggregateWithId(id);
        order.attributes().units().value(description.units());
        return order;
    }
}
