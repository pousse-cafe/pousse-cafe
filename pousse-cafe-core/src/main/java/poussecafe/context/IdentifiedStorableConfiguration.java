package poussecafe.context;

import poussecafe.storable.IdentifiedStorable;
import poussecafe.storable.IdentifiedStorableFactory;
import poussecafe.storable.IdentifiedStorableRepository;
import poussecafe.util.AbstractBuilder;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class IdentifiedStorableConfiguration<A extends IdentifiedStorable<?, ?>> {

    private Class<A> storableClass;

    private IdentifiedStorableRepository<?, ?, ?> repository;

    private IdentifiedStorableFactory<?, ?, ?> factory;

    public Class<?> getStorableClass() {
        return storableClass;
    }

    public IdentifiedStorableRepository<A, ?, ?> getRepository() {
        return (IdentifiedStorableRepository<A, ?, ?>) repository;
    }

    public IdentifiedStorableFactory<?, A, ?> getFactory() {
        return (IdentifiedStorableFactory<?, A, ?>) factory;
    }

    public static class Builder<A extends IdentifiedStorable<?, ?>> extends AbstractBuilder<IdentifiedStorableConfiguration<A>> {

        public Builder() {
            super(new IdentifiedStorableConfiguration<>());
        }

        public Builder<A> withStorableClass(Class<? extends A> storableClass) {
            product().storableClass = (Class<A>) storableClass;
            return this;
        }

        public Builder<A> withRepository(IdentifiedStorableRepository repository) {
            product().repository = repository;
            return this;
        }

        public Builder<A> withFactory(IdentifiedStorableFactory factory) {
            product().factory = factory;
            return this;
        }

        @Override
        protected void checkProduct(IdentifiedStorableConfiguration product) {
            checkThat(value(product.storableClass).notNull().because("Storable class cannot be null"));
            checkThat(value(product.repository).notNull().because("Repository class cannot be null"));
            checkThat(value(product.factory).notNull().because("Factory class cannot be null"));
        }
    }

    private IdentifiedStorableConfiguration() {

    }

}
