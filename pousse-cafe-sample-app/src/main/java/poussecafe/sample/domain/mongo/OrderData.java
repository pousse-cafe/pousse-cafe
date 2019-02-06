package poussecafe.sample.domain.mongo;

import org.springframework.data.annotation.Id;
import poussecafe.attribute.Attribute;
import poussecafe.sample.adapters.storage.SerializableOrderKey;
import poussecafe.sample.domain.Order;
import poussecafe.sample.domain.OrderKey;

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
