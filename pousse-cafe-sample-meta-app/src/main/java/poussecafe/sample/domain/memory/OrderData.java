package poussecafe.sample.domain.memory;

import java.io.Serializable;
import poussecafe.sample.domain.Order;
import poussecafe.sample.domain.OrderKey;
import poussecafe.storable.ConvertingProperty;
import poussecafe.storable.Property;
import poussecafe.storage.memory.InlineProperty;

public class OrderData implements Order.Data, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public Property<OrderKey> key() {
        return new ConvertingProperty<SerializableOrderKey, OrderKey>(productKey) {
            @Override
            protected OrderKey convertFrom(SerializableOrderKey from) {
                return from.toOrderKey();
            }

            @Override
            protected SerializableOrderKey convertTo(OrderKey to) {
                return new SerializableOrderKey(to);
            }
        };
    }

    private InlineProperty<SerializableOrderKey> productKey = new InlineProperty<>(SerializableOrderKey.class);

    @Override
    public void setUnits(int units) {
        this.units.set(units);
    }

    private InlineProperty<Integer> units = new InlineProperty<>(Integer.class);

    @Override
    public int getUnits() {
        return units.get();
    }

}
