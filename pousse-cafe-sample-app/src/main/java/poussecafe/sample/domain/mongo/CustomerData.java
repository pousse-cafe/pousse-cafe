package poussecafe.sample.domain.mongo;

import org.springframework.data.annotation.Id;
import poussecafe.attribute.Attribute;
import poussecafe.contextconfigurer.DataImplementation;
import poussecafe.sample.domain.Customer;
import poussecafe.sample.domain.CustomerKey;
import poussecafe.spring.mongo.storage.SpringMongoDbStorage;

@DataImplementation(
    entity = Customer.class,
    storageNames = SpringMongoDbStorage.NAME
)
public class CustomerData implements Customer.Attributes {

    @Override
    public Attribute<CustomerKey> key() {
        return new Attribute<CustomerKey>() {

            @Override
            public CustomerKey value() {
                return new CustomerKey(key);
            }

            @Override
            public void value(CustomerKey value) {
                key = value.getValue();
            }

        };
    }

    @Id
    private String key;
}
