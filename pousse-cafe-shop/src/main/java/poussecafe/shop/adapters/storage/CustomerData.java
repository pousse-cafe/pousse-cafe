package poussecafe.shop.adapters.storage;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.shop.domain.Customer;
import poussecafe.shop.domain.CustomerId;

@SuppressWarnings("serial")
public class CustomerData implements Customer.Attributes, Serializable {

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

    private String id;
}
