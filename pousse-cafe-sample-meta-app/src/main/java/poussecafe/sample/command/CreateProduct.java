package poussecafe.sample.command;

import java.util.Objects;
import poussecafe.sample.domain.ProductKey;

public class CreateProduct {

    private ProductKey productKey;

    public CreateProduct(ProductKey productKey) {
        setProductKey(productKey);
    }

    public ProductKey getProductKey() {
        return productKey;
    }

    public void setProductKey(ProductKey productKey) {
        Objects.requireNonNull(productKey);
        this.productKey = productKey;
    }

    @Override
    public String toString() {
        return "CreateProduct [productKey=" + productKey + "]";
    }

}
