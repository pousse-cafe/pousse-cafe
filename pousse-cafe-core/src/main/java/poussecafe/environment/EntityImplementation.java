package poussecafe.environment;

import java.util.Objects;
import java.util.function.Supplier;
import poussecafe.storage.Storage;

public class EntityImplementation {

    public static class Builder {

        public Builder() {
            implementation = new EntityImplementation();
        }

        private EntityImplementation implementation;

        public Builder withEntityClass(Class<?> entityClass) {
            implementation.entityClass = entityClass;
            return this;
        }

        public Builder withDataFactory(Supplier<Object> dataFactory) {
            implementation.dataFactory = dataFactory;
            return this;
        }

        public Builder withDataAccessFactory(Supplier<Object> dataAccessFactory) {
            implementation.dataAccessFactory = dataAccessFactory;
            return this;
        }

        public Builder withStorage(Storage storage) {
            implementation.storage = storage;
            return this;
        }

        public EntityImplementation build() {
            Objects.requireNonNull(implementation.entityClass);
            Objects.requireNonNull(implementation.dataFactory);
            if(implementation.dataAccessFactory != null) {
                Objects.requireNonNull(implementation.storage);
            }
            return implementation;
        }
    }

    private EntityImplementation() {

    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    private Class<?> entityClass;

    public Supplier<Object> getDataFactory() {
        return dataFactory;
    }

    private Supplier<Object> dataFactory;

    public boolean hasDataAccess() {
        return dataAccessFactory != null;
    }

    public Supplier<Object> dataAccessFactory() {
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
}
