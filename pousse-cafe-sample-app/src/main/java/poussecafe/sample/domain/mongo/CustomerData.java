package poussecafe.sample.domain.mongo;

import org.springframework.data.annotation.Id;
import poussecafe.sample.domain.Customer;
import poussecafe.sample.domain.CustomerKey;
import poussecafe.spring.mongo.storage.MongoData;
import poussecafe.storable.Property;

public class CustomerData extends MongoData<CustomerKey> implements Customer.Data {

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
