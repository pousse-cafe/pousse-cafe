package poussecafe.sample.domain.mongo;

import org.springframework.data.annotation.Id;
import poussecafe.property.Property;
import poussecafe.sample.domain.Customer;
import poussecafe.sample.domain.CustomerKey;

public class CustomerData implements Customer.Data {

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

    @Id
    private String key;
}
