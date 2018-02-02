package poussecafe.sample.domain.mongo;

import org.springframework.data.annotation.Id;
import poussecafe.sample.domain.Customer;
import poussecafe.sample.domain.CustomerKey;
import poussecafe.storable.BaseProperty;
import poussecafe.storable.Property;

public class CustomerData implements Customer.Data {

    @Override
    public Property<CustomerKey> key() {
        return new BaseProperty<CustomerKey>(CustomerKey.class) {
            @Override
            protected CustomerKey getValue() {
                return new CustomerKey(key);
            }

            @Override
            protected void setValue(CustomerKey value) {
                key = value.getValue();
            }
        };
    }

    @Id
    private String key;
}
