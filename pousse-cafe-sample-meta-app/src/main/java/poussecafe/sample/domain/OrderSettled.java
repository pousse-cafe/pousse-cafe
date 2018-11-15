package poussecafe.sample.domain;

import poussecafe.domain.DomainEvent;

public class OrderSettled implements DomainEvent {

    private OrderKey orderKey;

    public OrderSettled(OrderKey key) {
        setOrderKey(key);
    }

    public OrderKey getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(OrderKey orderKey) {
        this.orderKey = orderKey;
    }

}
