package poussecafe.sample.domain.data;

import java.io.Serializable;
import poussecafe.inmemory.InlineProperty;
import poussecafe.sample.domain.Customer;
import poussecafe.sample.domain.CustomerKey;
import poussecafe.storable.ConvertingProperty;
import poussecafe.storable.Property;

public class CustomerData implements Customer.Data, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public Property<CustomerKey> key() {
        return new ConvertingProperty<String, CustomerKey>(key, CustomerKey.class) {
            @Override
            protected CustomerKey convertFrom(String from) {
                return new CustomerKey(from);
            }

            @Override
            protected String convertTo(CustomerKey to) {
                return to.getValue();
            }

        };
    }

    private InlineProperty<String> key = new InlineProperty<>(String.class);
}
