package poussecafe.shop.adapters.messaging;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.shop.domain.OrderKey;
import poussecafe.shop.domain.OrderReadyForShipping;

@MessageImplementation(message = OrderReadyForShipping.class)
@SuppressWarnings("serial")
public class SerializableOrderReadyForShipping implements Serializable, OrderReadyForShipping {

    @Override
    public Attribute<OrderKey> orderKey() {
        return AttributeBuilder.single(OrderKey.class)
                .from(SerializableOrderKey.class)
                .adapt(SerializableOrderKey::adapt)
                .get(() -> orderKey)
                .adapt(SerializableOrderKey::adapt)
                .set(value -> orderKey = value)
                .build();
    }

    private SerializableOrderKey orderKey;
}
