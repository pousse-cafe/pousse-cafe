package poussecafe.shop.adapters.messaging;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.shop.command.CreateCustomer;
import poussecafe.shop.domain.CustomerKey;

@MessageImplementation(message = CreateCustomer.class)
@SuppressWarnings("serial")
public class CreateCustomerData implements Serializable, CreateCustomer {

    @Override
    public Attribute<CustomerKey> customerKey() {
        return AttributeBuilder.stringKey(CustomerKey.class)
                .get(() -> customerId)
                .set(value -> customerId = value)
                .build();
    }

    private String customerId;
}
