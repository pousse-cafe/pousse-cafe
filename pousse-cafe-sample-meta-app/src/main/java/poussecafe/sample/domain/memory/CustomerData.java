package poussecafe.sample.domain.memory;

import java.io.Serializable;
import poussecafe.property.Property;
import poussecafe.sample.domain.Customer;
import poussecafe.sample.domain.CustomerKey;

@SuppressWarnings("serial")
public class CustomerData implements Customer.Data, Serializable {

    @Override
    public Property<CustomerKey> key() {
        return new Property<CustomerKey>() {
            @Override
            public CustomerKey get() {
                return new CustomerKey(key);
            }

            @Override
            public void set(CustomerKey value) {
                key = value.getValue();
            }

        };
    }

    private String key;
}
