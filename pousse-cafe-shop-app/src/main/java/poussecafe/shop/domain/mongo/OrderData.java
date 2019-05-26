package poussecafe.shop.domain.mongo;

import org.springframework.data.annotation.Id;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.shop.adapters.storage.OrderIdData;
import poussecafe.shop.domain.Order;
import poussecafe.shop.domain.OrderId;

public class OrderData implements Order.Attributes {

    @Override
    public Attribute<OrderId> identifier() {
        return AttributeBuilder.single(OrderId.class)
                .usingAutoAdapter(OrderIdData.class)
                .read(() -> id)
                .write(value -> id = value)
                .build();
    }

    @Id
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
