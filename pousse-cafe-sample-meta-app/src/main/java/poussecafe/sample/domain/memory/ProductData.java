package poussecafe.sample.domain.memory;

import java.io.Serializable;
import poussecafe.property.Property;
import poussecafe.sample.domain.Product;
import poussecafe.sample.domain.ProductKey;

@SuppressWarnings("serial")
public class ProductData implements Product.Data, Serializable {

    @Override
    public Property<ProductKey> key() {
        return new Property<ProductKey>() {
            @Override
            public ProductKey get() {
                return new ProductKey(productKey);
            }

            @Override
            public void set(ProductKey value) {
                productKey = value.getValue();
            }
        };
    }

    private String productKey;

    @Override
    public void setTotalUnits(int units) {
        totalUnits = units;
    }

    private int totalUnits;

    @Override
    public int getTotalUnits() {
        return totalUnits;
    }

    @Override
    public void setAvailableUnits(int units) {
        availableUnits = units;
    }

    private int availableUnits;

    @Override
    public int getAvailableUnits() {
        return availableUnits;
    }

}
