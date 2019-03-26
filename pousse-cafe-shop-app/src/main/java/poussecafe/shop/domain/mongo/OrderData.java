package poussecafe.shop.domain.mongo;

import org.springframework.data.annotation.Id;
import poussecafe.attribute.Attribute;
import poussecafe.shop.adapters.storage.SerializableOrderKey;
import poussecafe.shop.domain.Order;
import poussecafe.shop.domain.OrderKey;

public class OrderData implements Order.Attributes {

    @Override
    public Attribute<OrderKey> key() {
        return new Attribute<OrderKey>() {
            @Override
            public OrderKey value() {
                return key.toOrderKey();
            }

            @Override
            public void value(OrderKey value) {
                key = new SerializableOrderKey(value);
            }
        };
    }

    @Id
    private SerializableOrderKey key;

    @Override
    public void setUnits(int units) {
        this.units = units;
    }

    private int units;

    @Override
    public int getUnits() {
        return units;
    }

}
