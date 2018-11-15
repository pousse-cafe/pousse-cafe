package poussecafe.sample.adapters.storage;

import java.io.Serializable;
import poussecafe.property.Property;
import poussecafe.sample.domain.Order;
import poussecafe.sample.domain.OrderKey;

@SuppressWarnings("serial")
public class OrderData implements Order.Data, Serializable {

    @Override
    public Property<OrderKey> key() {
        return new Property<OrderKey>() {
            @Override
            public OrderKey get() {
                return productKey.toOrderKey();
            }

            @Override
            public void set(OrderKey value) {
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
