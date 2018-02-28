package poussecafe.sample.domain.memory;

import java.io.Serializable;
import poussecafe.sample.domain.Order;
import poussecafe.sample.domain.OrderKey;
import poussecafe.storable.Property;
import poussecafe.storage.memory.InMemoryActiveData;

public class OrderData extends InMemoryActiveData<OrderKey> implements Order.Data, Serializable {

    private static final long serialVersionUID = 1L;

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
