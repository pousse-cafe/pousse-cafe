package poussecafe.shop.domain.mongo;

import org.springframework.data.annotation.Id;
import poussecafe.attribute.Attribute;
import poussecafe.discovery.DataImplementation;
import poussecafe.shop.domain.Customer;
import poussecafe.shop.domain.CustomerId;
import poussecafe.spring.mongo.storage.SpringMongoDbStorage;

@DataImplementation(
    entity = Customer.class,
    storageNames = SpringMongoDbStorage.NAME
)
public class CustomerData implements Customer.Attributes {

    @Override
    public Attribute<CustomerId> identifier() {
        return new Attribute<CustomerId>() {

            @Override
            public CustomerId value() {
                return new CustomerId(id);
            }

            @Override
            public void value(CustomerId value) {
                id = value.getValue();
            }

        };
    }

    @Id
    private String id;
}
