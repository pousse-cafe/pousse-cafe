package poussecafe.sample.domain.data;

import java.io.Serializable;
import poussecafe.inmemory.InlineProperty;
import poussecafe.sample.domain.Product;
import poussecafe.sample.domain.ProductKey;
import poussecafe.storable.ConvertingProperty;
import poussecafe.storable.Property;

public class ProductData implements Product.Data, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public Property<ProductKey> key() {
        return new ConvertingProperty<String, ProductKey>(productKey, ProductKey.class) {
            @Override
            protected ProductKey convertFrom(String from) {
                return new ProductKey(from);
            }

            @Override
            protected String convertTo(ProductKey to) {
                return to.getValue();
            }
        };
    }

    private InlineProperty<String> productKey = new InlineProperty<>(String.class);

    @Override
    public void setTotalUnits(int units) {
        totalUnits.set(units);
    }

    private InlineProperty<Integer> totalUnits = new InlineProperty<>(Integer.class);

    @Override
    public int getTotalUnits() {
        return totalUnits.get();
    }

    @Override
    public void setAvailableUnits(int units) {
        availableUnits.set(units);
    }

    private InlineProperty<Integer> availableUnits = new InlineProperty<>(Integer.class);

    @Override
    public int getAvailableUnits() {
        return availableUnits.get();
    }

}
