package poussecafe.shop.adapters.messaging;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.shop.domain.OrderDescription;
import poussecafe.shop.domain.OrderRejected;
import poussecafe.shop.domain.ProductKey;

@MessageImplementation(message = OrderRejected.class)
@SuppressWarnings("serial")
public class SerializableOrderRejected implements Serializable, OrderRejected {

    @Override
    public Attribute<ProductKey> productKey() {
        return AttributeBuilder.single(ProductKey.class)
                .from(String.class)
                .adapt(ProductKey::new)
                .get(() -> productId)
                .adapt(ProductKey::getValue)
                .set(value -> productId = value)
                .build();
    }

    private String productId;

    @Override
    public Attribute<OrderDescription> description() {
        return AttributeBuilder.single(OrderDescription.class)
                .fromAutoAdapting(OrderDescriptionData.class)
                .get(() -> description)
                .set(value -> description = value)
                .build();
    }

    private OrderDescriptionData description;
}
