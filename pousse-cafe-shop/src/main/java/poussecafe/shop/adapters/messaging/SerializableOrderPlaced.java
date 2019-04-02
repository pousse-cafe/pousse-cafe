package poussecafe.shop.adapters.messaging;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.shop.domain.OrderDescription;
import poussecafe.shop.domain.OrderPlaced;
import poussecafe.shop.domain.ProductId;

@MessageImplementation(message = OrderPlaced.class)
@SuppressWarnings("serial")
public class SerializableOrderPlaced implements Serializable, OrderPlaced {

    @Override
    public Attribute<ProductId> productId() {
        return AttributeBuilder.single(ProductId.class)
                .from(String.class)
                .adapt(ProductId::new)
                .get(() -> productId)
                .adapt(ProductId::getValue)
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
