package poussecafe.shop.command;

import java.util.Objects;
import poussecafe.shop.domain.ProductKey;

public class AddUnits {

    private ProductKey productKey;

    private int units;

    public AddUnits(ProductKey productKey, int availableUnits) {
        setProductKey(productKey);
        setUnits(availableUnits);
    }

    public ProductKey getProductKey() {
        return productKey;
    }

    private void setProductKey(ProductKey productKey) {
        Objects.requireNonNull(productKey);
        this.productKey = productKey;
    }

    public int getUnits() {
        return units;
    }

    private void setUnits(int units) {
        if(units < 0) {
            throw new IllegalArgumentException("Units must be >=0");
        }
        this.units = units;
    }

    @Override
    public String toString() {
        return "AddUnits [productKey=" + productKey + ", units=" + units + "]";
    }

}
