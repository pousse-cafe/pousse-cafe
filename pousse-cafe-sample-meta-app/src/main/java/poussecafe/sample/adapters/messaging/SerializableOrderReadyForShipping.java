package poussecafe.sample.adapters.messaging;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.contextconfigurer.MessageImplementation;
import poussecafe.sample.domain.OrderKey;
import poussecafe.sample.domain.OrderReadyForShipping;

@MessageImplementation(message = OrderReadyForShipping.class)
@SuppressWarnings("serial")
public class SerializableOrderReadyForShipping implements Serializable, OrderReadyForShipping {

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
