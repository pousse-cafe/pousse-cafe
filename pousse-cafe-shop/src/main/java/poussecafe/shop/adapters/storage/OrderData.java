package poussecafe.shop.adapters.storage;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.shop.domain.Order;
import poussecafe.shop.domain.OrderKey;

@SuppressWarnings("serial")
public class OrderData implements Order.Attributes, Serializable {

    @Override
    public Attribute<OrderKey> key() {
        return AttributeBuilder.single(OrderKey.class)
                .fromAutoAdapting(OrderKeyData.class)
                .get(() -> key)
                .set(value -> key = value)
                .build();
    }

    private OrderKeyData key;

    @Override
    public Attribute<Integer> units() {
        return AttributeBuilder.single(Integer.class)
                .get(() -> units)
                .set(value -> units = value)
                .build();
    }

    private int units;
}
