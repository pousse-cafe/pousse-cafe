package poussecafe.domain;

import poussecafe.util.AbstractBuilder;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class EntityDefinition {

    public Class<?> getEntityClass() {
        return entityClass;
    }

    private Class<?> entityClass;

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

    public static class Builder extends AbstractBuilder<EntityDefinition> {

        public Builder() {
            super(new EntityDefinition());
        }

        public Builder withEntityClass(Class<?> entityClass) {
            product().entityClass = entityClass;
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
        protected void checkProduct(EntityDefinition product) {
            checkThat(value(product.entityClass).notNull().because("Entity class cannot be null"));
        }
    }

    private EntityDefinition() {

    }
}
