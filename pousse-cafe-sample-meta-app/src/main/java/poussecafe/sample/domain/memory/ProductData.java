package poussecafe.sample.domain.memory;

import poussecafe.sample.domain.Product;
import poussecafe.sample.domain.ProductKey;
import poussecafe.storable.Property;
import poussecafe.storage.memory.InMemoryActiveData;

public class ProductData extends InMemoryActiveData<ProductKey> implements Product.Data {

    private static final long serialVersionUID = 1L;

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
