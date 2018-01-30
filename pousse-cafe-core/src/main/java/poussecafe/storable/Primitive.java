package poussecafe.storable;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class Primitive {

    void setPrimitiveFactory(PrimitiveFactory primitiveFactory) {
        checkThat(value(primitiveFactory).notNull());
        this.primitiveFactory = primitiveFactory;
    }

    private PrimitiveFactory primitiveFactory;

    protected <T> T newPrimitive(Class<T> primitiveClass) {
        return primitiveFactory.newPrimitive(
                new PrimitiveSpecification.Builder<T>().withPrimitiveClass(primitiveClass).withData(true).build());
    }

    protected <T> T newPrimitive(PrimitiveSpecification<T> specification) {
        return primitiveFactory.newPrimitive(specification);
    }
}
