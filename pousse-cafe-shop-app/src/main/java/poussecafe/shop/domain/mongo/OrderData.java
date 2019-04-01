package poussecafe.shop.domain.mongo;

import org.springframework.data.annotation.Id;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.shop.adapters.storage.OrderKeyData;
import poussecafe.shop.domain.Order;
import poussecafe.shop.domain.OrderKey;

public class OrderData implements Order.Attributes {

    @Override
    public Attribute<OrderKey> key() {
        return AttributeBuilder.single(OrderKey.class)
                .fromAutoAdapting(OrderKeyData.class)
                .get(() -> key)
                .set(value -> key = value)
                .build();
    }

    @Id
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
