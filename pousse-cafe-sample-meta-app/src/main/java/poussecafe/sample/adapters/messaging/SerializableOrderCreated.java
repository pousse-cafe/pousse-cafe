package poussecafe.sample.adapters.messaging;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.contextconfigurer.MessageImplementation;
import poussecafe.sample.domain.OrderCreated;
import poussecafe.sample.domain.OrderKey;

@MessageImplementation(message = OrderCreated.class)
@SuppressWarnings("serial")
public class SerializableOrderCreated implements Serializable, OrderCreated {

    @Override
    public Attribute<OrderKey> orderKey() {
        return AttributeBuilder.simple(OrderKey.class)
                .from(SerializableOrderKey.class)
                .adapt(SerializableOrderKey::adapt)
                .get(() -> orderKey)
                .adapt(SerializableOrderKey::adapt)
                .set(value -> orderKey = value)
                .build();
    }

    private SerializableOrderKey orderKey;
}
