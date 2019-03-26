package poussecafe.sample.command;

import java.util.Objects;
import poussecafe.sample.domain.OrderKey;

public class ShipOrder {

    private OrderKey orderKey;

    public ShipOrder(OrderKey orderKey) {
        setOrderKey(orderKey);
    }

    public OrderKey getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(OrderKey orderKey) {
        Objects.requireNonNull(orderKey);
        this.orderKey = orderKey;
    }
}
