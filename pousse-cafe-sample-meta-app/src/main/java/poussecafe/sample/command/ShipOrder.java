package poussecafe.sample.command;

import poussecafe.sample.domain.OrderKey;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class ShipOrder {

    private OrderKey orderKey;

    public ShipOrder(OrderKey orderKey) {
        setOrderKey(orderKey);
    }

    public OrderKey getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(OrderKey orderKey) {
        checkThat(value(orderKey).notNull().because("Order key cannot be null"));
        this.orderKey = orderKey;
    }
}
