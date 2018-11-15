package poussecafe.sample.domain.mongo;

import org.springframework.data.annotation.Id;
import poussecafe.property.Property;
import poussecafe.sample.adapters.storage.SerializableOrderKey;
import poussecafe.sample.domain.Order;
import poussecafe.sample.domain.OrderKey;

public class OrderData implements Order.Data {

    @Override
    public Property<OrderKey> key() {
        return new Property<OrderKey>() {
            @Override
            public OrderKey get() {
                return key.toOrderKey();
            }

            @Override
            public void set(OrderKey value) {
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
