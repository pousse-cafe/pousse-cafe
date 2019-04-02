package poussecafe.shop.adapters.messaging;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.shop.command.CreateCustomer;
import poussecafe.shop.domain.CustomerId;

@MessageImplementation(message = CreateCustomer.class)
@SuppressWarnings("serial")
public class CreateCustomerData implements Serializable, CreateCustomer {

    @Override
    public Attribute<CustomerId> customerId() {
        return AttributeBuilder.stringId(CustomerId.class)
                .get(() -> customerId)
                .set(value -> customerId = value)
                .build();
    }

    private String customerId;
}
