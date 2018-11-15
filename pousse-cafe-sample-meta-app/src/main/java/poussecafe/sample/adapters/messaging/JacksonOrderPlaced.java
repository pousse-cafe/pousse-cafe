package poussecafe.sample.adapters.messaging;

import poussecafe.property.Property;
import poussecafe.property.PropertyBuilder;
import poussecafe.sample.domain.OrderDescription;
import poussecafe.sample.domain.OrderPlaced;
import poussecafe.sample.domain.ProductKey;

public class JacksonOrderPlaced implements OrderPlaced {

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
                .get(() -> description)
                .set(value -> description = value)
                .build();
    }

    private OrderDescription description;
}
