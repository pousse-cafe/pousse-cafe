package poussecafe.shop.adapters.messaging;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.shop.command.CreateProduct;
import poussecafe.shop.domain.ProductKey;

@MessageImplementation(message = CreateProduct.class)
@SuppressWarnings("serial")
public class CreateProductData implements Serializable, CreateProduct {

    @Override
    public Attribute<ProductKey> productKey() {
        return AttributeBuilder.stringKey(ProductKey.class)
                .get(() -> productId)
                .set(value -> productId = value)
                .build();
    }

    private String productId;
}
