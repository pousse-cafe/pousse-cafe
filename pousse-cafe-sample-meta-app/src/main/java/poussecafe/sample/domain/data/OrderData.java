package poussecafe.sample.domain.data;

import java.io.Serializable;
import poussecafe.inmemory.BaseProperty;
import poussecafe.inmemory.InlineProperty;
import poussecafe.sample.domain.CustomerKey;
import poussecafe.sample.domain.Order;
import poussecafe.sample.domain.OrderKey;
import poussecafe.sample.domain.ProductKey;
import poussecafe.storable.Property;

public class OrderData implements Order.Data, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public Property<OrderKey> key() {
        return new BaseProperty<OrderKey>(OrderKey.class) {
            @Override
            protected OrderKey getValue() {
                return new OrderKey(new ProductKey(productKey.get()), new CustomerKey(customerKey.get()),
                        reference.get());
            }

            @Override
            protected void setValue(OrderKey value) {
                productKey.set(value.getProductKey().getValue());
                customerKey.set(value.getCustomerKey().getValue());
                reference.set(value.getReference());
            }
        };
    }

    private InlineProperty<String> productKey = new InlineProperty<>(String.class);

    private InlineProperty<String> customerKey = new InlineProperty<>(String.class);

    private InlineProperty<String> reference = new InlineProperty<>(String.class);

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
