package poussecafe.shop.adapters.storage;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.shop.domain.Product;
import poussecafe.shop.domain.ProductKey;

@SuppressWarnings("serial")
public class ProductData implements Product.Attributes, Serializable {

    @Override
    public Attribute<ProductKey> key() {
        return new Attribute<ProductKey>() {
            @Override
            public ProductKey value() {
                return new ProductKey(productKey);
            }

            @Override
            public void value(ProductKey value) {
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
