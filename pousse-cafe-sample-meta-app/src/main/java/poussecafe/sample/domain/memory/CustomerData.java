package poussecafe.sample.domain.memory;

import poussecafe.sample.domain.Customer;
import poussecafe.sample.domain.CustomerKey;
import poussecafe.storable.Property;
import poussecafe.storage.memory.InMemoryActiveData;

public class CustomerData extends InMemoryActiveData<CustomerKey> implements Customer.Data {

    private static final long serialVersionUID = 1L;

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
