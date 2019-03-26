package poussecafe.shop.adapters.storage;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.shop.domain.Customer;
import poussecafe.shop.domain.CustomerKey;

@SuppressWarnings("serial")
public class CustomerData implements Customer.Attributes, Serializable {

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

    private String key;
}
