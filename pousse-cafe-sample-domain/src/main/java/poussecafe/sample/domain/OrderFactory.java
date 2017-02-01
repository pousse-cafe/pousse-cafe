package poussecafe.sample.domain;

import poussecafe.domain.Factory;
import poussecafe.sample.domain.Order.OrderData;

public class OrderFactory extends Factory<OrderKey, Order, OrderData> {

    public Order buildPlacedOrder(OrderKey orderKey,
            ProductKey productKey,
            int units) {
        Order order = newAggregateWithKey(orderKey);
        order.setProductKey(productKey);
        order.setUnits(units);
        return order;
    }

    @Override
    protected Order newAggregate() {
        return new Order();
    }
}
