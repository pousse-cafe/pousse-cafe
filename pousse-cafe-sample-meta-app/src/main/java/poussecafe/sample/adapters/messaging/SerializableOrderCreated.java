package poussecafe.sample.adapters.messaging;

import java.io.Serializable;
import poussecafe.messaging.MessageImplementation;
import poussecafe.property.Property;
import poussecafe.property.PropertyBuilder;
import poussecafe.sample.domain.OrderCreated;
import poussecafe.sample.domain.OrderKey;

@MessageImplementation(message = OrderCreated.class)
@SuppressWarnings("serial")
public class SerializableOrderCreated implements Serializable, OrderCreated {

    @Override
    public Property<OrderKey> orderKey() {
        return PropertyBuilder.simple(OrderKey.class)
                .from(SerializableOrderKey.class)
                .adapt(SerializableOrderKey::adapt)
                .get(() -> orderKey)
                .adapt(SerializableOrderKey::adapt)
                .set(value -> orderKey = value)
                .build();
    }

    private SerializableOrderKey orderKey;
}
