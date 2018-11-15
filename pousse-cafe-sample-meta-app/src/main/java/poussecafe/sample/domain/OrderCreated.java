package poussecafe.sample.domain;

import poussecafe.domain.DomainEvent;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class OrderCreated implements DomainEvent {

    private OrderKey orderKey;

    public OrderCreated(OrderKey key) {
        setOrderKey(key);
    }

    public OrderKey getOrderKey() {
        return orderKey;
    }

    private void setOrderKey(OrderKey orderKey) {
        checkThat(value(orderKey).notNull().because("Order key cannot be null"));
        this.orderKey = orderKey;
    }

}
