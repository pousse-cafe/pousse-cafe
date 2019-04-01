package poussecafe.shop.adapters.messaging;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.shop.command.PlaceOrder;
import poussecafe.shop.domain.OrderDescription;
import poussecafe.shop.domain.ProductKey;

@MessageImplementation(message = PlaceOrder.class)
@SuppressWarnings("serial")
public class PlaceOrderData implements Serializable, PlaceOrder {

    @Override
    public Attribute<ProductKey> productKey() {
        return AttributeBuilder.stringKey(ProductKey.class)
                .get(() -> productId)
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
