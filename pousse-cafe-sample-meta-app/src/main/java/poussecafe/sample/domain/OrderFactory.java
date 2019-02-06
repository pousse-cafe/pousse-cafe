package poussecafe.sample.domain;

import poussecafe.domain.Factory;

public class OrderFactory extends Factory<OrderKey, Order, Order.Attributes> {

    public Order buildPlacedOrder(OrderKey orderKey,
            int units) {
        Order order = newAggregateWithKey(orderKey);
        order.setUnits(units);
        return order;
    }
}
