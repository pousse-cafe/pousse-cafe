package poussecafe.storable;

import poussecafe.util.AbstractBuilder;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class StorableDefinition {

    public Class<?> getStorableClass() {
        return storableClass;
    }

    private Class<?> storableClass;

    public boolean hasFactory() {
        return factoryClass != null;
    }

    public Class getFactoryClass() {
        return factoryClass;
    }

    private Class<?> factoryClass;

    public Class getRepositoryClass() {
        return repositoryClass;
    }

    private Class<?> repositoryClass;

    public boolean hasRepository() {
        return repositoryClass != null;
    }

    public static class Builder extends AbstractBuilder<StorableDefinition> {

        public Builder() {
            super(new StorableDefinition());
        }

        public Builder withStorableClass(Class<?> storableClass) {
            product().storableClass = storableClass;
            return this;
        }

        public Builder withFactoryClass(Class<?> factoryClass) {
            product().factoryClass = factoryClass;
            return this;
        }

        public Builder withRepositoryClass(Class<?> repositoryClass) {
            product().repositoryClass = repositoryClass;
            return this;
        }

        @Override
        protected void checkProduct(StorableDefinition product) {
            checkThat(value(product.storableClass).notNull().because("Storable class cannot be null"));
        }
    }

    private StorableDefinition() {

    }
}
