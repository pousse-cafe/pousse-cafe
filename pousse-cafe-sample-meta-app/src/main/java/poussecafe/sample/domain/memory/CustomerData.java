package poussecafe.sample.domain.memory;

import java.io.Serializable;
import poussecafe.sample.domain.Customer;
import poussecafe.sample.domain.CustomerKey;
import poussecafe.storable.ConvertingProperty;
import poussecafe.storable.Property;
import poussecafe.storage.memory.InlineProperty;

public class CustomerData implements Customer.Data, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public Property<CustomerKey> key() {
        return new ConvertingProperty<String, CustomerKey>(key) {
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
