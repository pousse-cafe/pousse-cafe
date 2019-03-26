package poussecafe.shop.adapters.storage;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.shop.domain.Order;
import poussecafe.shop.domain.OrderKey;

@SuppressWarnings("serial")
public class OrderData implements Order.Attributes, Serializable {

    @Override
    public Attribute<OrderKey> key() {
        return new Attribute<OrderKey>() {
            @Override
            public OrderKey value() {
                return productKey.toOrderKey();
            }

            @Override
            public void value(OrderKey value) {
                productKey = new SerializableOrderKey(value);
            }
        };
    }

    private SerializableOrderKey productKey;

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
