package poussecafe.shop.adapters.messaging;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.shop.command.AddUnits;
import poussecafe.shop.domain.ProductId;

@MessageImplementation(message = AddUnits.class)
@SuppressWarnings("serial")
public class AddUnitsData implements Serializable, AddUnits {

    @Override
    public Attribute<ProductId> productId() {
        return AttributeBuilder.stringId(ProductId.class)
                .read(() -> productId)
                .write(value -> productId = value)
                .build();
    }

    private String productId;

    @Override
    public Attribute<Integer> units() {
        return AttributeBuilder.single(Integer.class)
                .read(() -> units)
                .write(value -> units = value)
                .build();
    }

    private int units;
}
