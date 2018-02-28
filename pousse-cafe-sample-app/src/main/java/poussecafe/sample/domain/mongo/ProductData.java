package poussecafe.sample.domain.mongo;

import org.springframework.data.annotation.Id;
import poussecafe.sample.domain.Product;
import poussecafe.sample.domain.ProductKey;
import poussecafe.spring.mongo.storage.MongoData;
import poussecafe.storable.Property;

public class ProductData extends MongoData<ProductKey> implements Product.Data {

    @Override
    public Property<ProductKey> key() {
        return new Property<ProductKey>() {
            @Override
            public ProductKey get() {
                return new ProductKey(key);
            }

            @Override
            public void set(ProductKey value) {
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
