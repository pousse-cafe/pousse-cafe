package poussecafe.sample.domain;

import poussecafe.domain.Factory;
import poussecafe.sample.domain.Order.Data;

public class OrderFactory extends Factory<OrderKey, Order, Data> {

    public Order buildPlacedOrder(OrderKey orderKey,
            int units) {
        Order order = newAggregateWithKey(orderKey);
        order.setUnits(units);
        return order;
    }

    @Override
    protected Order newAggregate() {
        return new Order();
    }
}
