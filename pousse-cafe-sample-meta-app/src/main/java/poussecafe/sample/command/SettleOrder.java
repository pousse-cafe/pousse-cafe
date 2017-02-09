package poussecafe.sample.command;

import poussecafe.consequence.Command;
import poussecafe.sample.domain.OrderKey;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class SettleOrder extends Command {

    private OrderKey orderKey;

    public SettleOrder(OrderKey orderKey) {
        setOrderKey(orderKey);
    }

    public OrderKey getOrderKey() {
        return orderKey;
    }

    private void setOrderKey(OrderKey orderKey) {
        checkThat(value(orderKey).notNull().because("Product key cannot be null"));
        this.orderKey = orderKey;
    }
}
