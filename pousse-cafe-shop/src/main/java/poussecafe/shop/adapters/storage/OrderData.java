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
                .usingAutoAdapter(OrderIdData.class)
                .read(() -> id)
                .write(value -> id = value)
                .build();
    }

    private OrderIdData id;

    @Override
    public Attribute<Integer> units() {
        return AttributeBuilder.single(Integer.class)
                .read(() -> units)
                .write(value -> units = value)
                .build();
    }

    private int units;
}
