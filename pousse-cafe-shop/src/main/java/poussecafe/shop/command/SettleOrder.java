package poussecafe.shop.command;

import java.util.Objects;
import poussecafe.shop.domain.OrderKey;

public class SettleOrder {

    private OrderKey orderKey;

    public SettleOrder(OrderKey orderKey) {
        setOrderKey(orderKey);
    }

    public OrderKey getOrderKey() {
        return orderKey;
    }

    private void setOrderKey(OrderKey orderKey) {
        Objects.requireNonNull(orderKey);
        this.orderKey = orderKey;
    }
}
