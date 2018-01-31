package poussecafe.context;

import poussecafe.storable.IdentifiedStorableDataAccess;
import poussecafe.storable.PrimitiveFactory;
import poussecafe.storage.Storage;
import poussecafe.util.AbstractBuilder;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class IdentifiedStorableBackend {

    private Class storableClass;

    private Storage storage;

    private IdentifiedStorableDataAccess dataAccess;

    private PrimitiveFactory storableFactory;

    public Class getStorableClass() {
        return storableClass;
    }

    public Storage getStorage() {
        return storage;
    }

    public IdentifiedStorableDataAccess getDataAccess() {
        return dataAccess;
    }

    public PrimitiveFactory getStorableFactory() {
        return storableFactory;
    }

    public static class Builder extends AbstractBuilder<IdentifiedStorableBackend> {

        protected Builder() {
            super(new IdentifiedStorableBackend());
        }

        @Override
        protected void checkProduct(IdentifiedStorableBackend product) {
            checkThat(value(product.storableClass).notNull().because("Storable class cannot be null"));
            checkThat(value(product.storage).notNull().because("Storage cannot be null"));
            checkThat(value(product.dataAccess).notNull().because("Data access cannot be null"));
            checkThat(value(product.storableFactory).notNull().because("Storable factory cannot be null"));
        }

    }

    private IdentifiedStorableBackend() {

    }
}
