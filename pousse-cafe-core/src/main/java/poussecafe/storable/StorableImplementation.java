package poussecafe.storable;

import java.util.function.Supplier;
import poussecafe.storage.Storage;
import poussecafe.util.AbstractBuilder;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class StorableImplementation {

    public Class<?> getStorableClass() {
        return storableClass;
    }

    private Class<?> storableClass;

    public Supplier<Object> getDataFactory() {
        return dataFactory;
    }

    private Supplier<Object> dataFactory;

    public boolean hasDataAccess() {
        return dataAccessFactory != null;
    }

    public Supplier<Object> getDataAccessFactory() {
        return dataAccessFactory;
    }

    private Supplier<Object> dataAccessFactory;

    public Storage getStorage() {
        return storage;
    }

    private Storage storage;

    public boolean hasStorage() {
        return storage != null;
    }

    public static class Builder extends AbstractBuilder<StorableImplementation> {

        public Builder() {
            super(new StorableImplementation());
        }

        public Builder withStorableClass(Class<?> storableClass) {
            product().storableClass = storableClass;
            return this;
        }

        public Builder withDataFactory(Supplier<Object> dataFactory) {
            product().dataFactory = dataFactory;
            return this;
        }

        public Builder withDataAccessFactory(Supplier<Object> dataAccessFactory) {
            product().dataAccessFactory = dataAccessFactory;
            return this;
        }

        public Builder withStorage(Storage storage) {
            product().storage = storage;
            return this;
        }

        @Override
        protected void checkProduct(StorableImplementation product) {
            checkThat(value(product.storableClass).notNull().because("Storable class cannot be null"));
            checkThat(value(product.dataFactory).notNull().because("Storable data factory cannot be null"));
            if(product.dataAccessFactory != null) {
                checkThat(value(product.storage).notNull().because("Storage cannot be null"));
            }
        }

    }

    private StorableImplementation() {

    }
}
