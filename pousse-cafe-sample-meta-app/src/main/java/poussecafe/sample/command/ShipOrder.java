package poussecafe.sample.command;

import poussecafe.messaging.Command;
import poussecafe.sample.domain.OrderKey;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class ShipOrder extends Command {

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
