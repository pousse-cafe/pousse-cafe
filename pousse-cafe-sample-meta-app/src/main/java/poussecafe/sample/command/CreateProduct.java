package poussecafe.sample.command;

import poussecafe.consequence.Command;
import poussecafe.sample.domain.ProductKey;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class CreateProduct extends Command {

    private ProductKey productKey;

    public CreateProduct(ProductKey productKey) {
        setProductKey(productKey);
    }

    public ProductKey getProductKey() {
        return productKey;
    }

    public void setProductKey(ProductKey productKey) {
        checkThat(value(productKey).notNull().because("Product key cannot be null"));
        this.productKey = productKey;
    }

    @Override
    public String toString() {
        return "CreateProduct [productKey=" + productKey + "]";
    }

}
