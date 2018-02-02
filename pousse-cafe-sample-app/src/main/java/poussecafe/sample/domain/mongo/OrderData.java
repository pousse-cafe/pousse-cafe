package poussecafe.sample.domain.mongo;

import org.springframework.data.annotation.Id;
import poussecafe.sample.domain.Order;
import poussecafe.sample.domain.OrderKey;
import poussecafe.storable.BaseProperty;
import poussecafe.storable.Property;

public class OrderData implements Order.Data {

    @Override
    public Property<OrderKey> key() {
        return new BaseProperty<OrderKey>(OrderKey.class) {
            @Override
            protected OrderKey getValue() {
                return key;
            }

            @Override
            protected void setValue(OrderKey value) {
                key = value;
            }
        };
    }

    @Id
    private OrderKey key;

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
