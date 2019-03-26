package poussecafe.shop.domain.mongo;

import org.springframework.data.annotation.Id;
import poussecafe.attribute.Attribute;
import poussecafe.shop.domain.Product;
import poussecafe.shop.domain.ProductKey;

public class ProductData implements Product.Attributes {

    @Override
    public Attribute<ProductKey> key() {
        return new Attribute<ProductKey>() {
            @Override
            public ProductKey value() {
                return new ProductKey(key);
            }

            @Override
            public void value(ProductKey value) {
                key = value.getValue();
            }
        };
    }

    @Id
    private String key;

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
