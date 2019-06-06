package poussecafe.shop.domain.mongo;

import org.springframework.data.annotation.Id;
import poussecafe.attribute.Attribute;
import poussecafe.shop.domain.Customer;
import poussecafe.shop.domain.CustomerId;

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
                id = value.stringValue();
            }

        };
    }

    @Id
    private String id;
}
