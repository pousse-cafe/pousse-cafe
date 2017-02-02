package poussecafe.sample.workflow;

import poussecafe.consequence.Command;
import poussecafe.sample.domain.ProductKey;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.equalTo;
import static poussecafe.check.Predicates.greaterThan;

public class AddUnits extends Command {

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
        checkThat(value(productKey).notNull().because("Product key cannot be null"));
        this.productKey = productKey;
    }

    public int getUnits() {
        return units;
    }

    private void setUnits(int units) {
        checkThat(value(units).verifies(greaterThan(0).or(equalTo(0))).because("Units must be >=0"));
        this.units = units;
    }

    @Override
    public String toString() {
        return "AddUnits [productKey=" + productKey + ", units=" + units + "]";
    }

}
