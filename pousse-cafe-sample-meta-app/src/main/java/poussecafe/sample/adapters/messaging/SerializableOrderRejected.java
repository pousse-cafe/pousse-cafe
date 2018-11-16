package poussecafe.sample.adapters.messaging;

import java.io.Serializable;
import poussecafe.property.Property;
import poussecafe.property.PropertyBuilder;
import poussecafe.sample.domain.OrderDescription;
import poussecafe.sample.domain.OrderRejected;
import poussecafe.sample.domain.ProductKey;

@SuppressWarnings("serial")
public class SerializableOrderRejected implements Serializable, OrderRejected {

    @Override
    public Property<ProductKey> productKey() {
        return PropertyBuilder.simple(ProductKey.class)
                .from(String.class)
                .adapt(ProductKey::new)
                .get(() -> productId)
                .adapt(ProductKey::getValue)
                .set(value -> productId = value)
                .build();
    }

    private String productId;

    @Override
    public Property<OrderDescription> description() {
        return PropertyBuilder.simple(OrderDescription.class)
                .from(SerializableOrderDescription.class)
                .adapt(SerializableOrderDescription::adapt)
                .get(() -> description)
                .adapt(SerializableOrderDescription::adapt)
                .set(value -> description = value)
                .build();
    }

    private SerializableOrderDescription description;
}
