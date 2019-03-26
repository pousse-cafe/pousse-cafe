package poussecafe.shop.domain.mongo;

import org.springframework.data.annotation.Id;
import poussecafe.attribute.Attribute;
import poussecafe.discovery.DataImplementation;
import poussecafe.shop.domain.Customer;
import poussecafe.shop.domain.CustomerKey;
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
