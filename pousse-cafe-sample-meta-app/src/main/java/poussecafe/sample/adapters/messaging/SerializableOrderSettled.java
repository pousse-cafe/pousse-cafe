package poussecafe.sample.adapters.messaging;

import java.io.Serializable;
import poussecafe.contextconfigurer.MessageImplementation;
import poussecafe.property.Property;
import poussecafe.property.PropertyBuilder;
import poussecafe.sample.domain.OrderKey;
import poussecafe.sample.domain.OrderSettled;

@MessageImplementation(message = OrderSettled.class)
@SuppressWarnings("serial")
public class SerializableOrderSettled implements Serializable, OrderSettled {

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
