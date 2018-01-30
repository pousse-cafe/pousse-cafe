package poussecafe.util;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public abstract class AbstractBuilder<T> {

    private T product;

    protected AbstractBuilder(T initialProduct) {
        checkThat(value(initialProduct).notNull().because("Product cannot be initially null"));
        this.product = initialProduct;
    }

    protected T product() {
        return product;
    }

    public T build() {
        T product = this.product;
        checkProduct(product);
        this.product = null;
        return product;
    }

    protected abstract void checkProduct(T product);
}
