package poussecafe.shop.adapters.storage;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.shop.domain.Order;
import poussecafe.shop.domain.OrderId;

@SuppressWarnings("serial")
public class OrderData implements Order.Attributes, Serializable {

    @Override
    public Attribute<OrderId> identifier() {
        return AttributeBuilder.single(OrderId.class)
                .fromAutoAdapting(OrderIdData.class)
                .get(() -> id)
                .set(value -> id = value)
                .build();
    }

    private OrderIdData id;

    @Override
    public Attribute<Integer> units() {
        return AttributeBuilder.single(Integer.class)
                .get(() -> units)
                .set(value -> units = value)
                .build();
    }

    private int units;
}
