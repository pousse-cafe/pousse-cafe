package poussecafe.shop.domain.mongo;

import org.springframework.data.annotation.Id;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.shop.domain.Product;
import poussecafe.shop.domain.ProductId;

public class ProductData implements Product.Attributes {

    @Override
    public Attribute<ProductId> identifier() {
        return AttributeBuilder.stringId(ProductId.class)
                .get(() -> productId)
                .set(value -> productId = value)
                .build();
    }

    @Id
    private String productId;

    @Override
    public Attribute<Integer> totalUnits() {
        return AttributeBuilder.single(Integer.class)
                .get(() -> totalUnits)
                .set(value -> totalUnits = value)
                .build();
    }

    private int totalUnits;

    @Override
    public Attribute<Integer> availableUnits() {
        return AttributeBuilder.single(Integer.class)
                .get(() -> availableUnits)
                .set(value -> availableUnits = value)
                .build();
    }

    private int availableUnits;
}
