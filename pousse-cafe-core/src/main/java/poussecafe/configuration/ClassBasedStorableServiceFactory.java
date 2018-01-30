package poussecafe.configuration;

import poussecafe.exception.PousseCafeException;
import poussecafe.storable.IdentifiedStorableFactory;
import poussecafe.storable.IdentifiedStorableRepository;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class ClassBasedStorableServiceFactory<F extends IdentifiedStorableFactory<?, ?, ?>, R extends IdentifiedStorableRepository<?, ?, ?>>
implements StorableServiceFactory<F, R> {

    private Class<F> factoryClass;

    private Class<R> repositoryClass;

    public ClassBasedStorableServiceFactory(Class<F> factoryClass, Class<R> repositoryClass) {
        setFactoryClass(factoryClass);
        setRepositoryClass(repositoryClass);
    }

    private void setFactoryClass(Class<F> factoryClass) {
        checkThat(value(factoryClass).notNull().because("Factory class cannot be null"));
        this.factoryClass = factoryClass;
    }

    private void setRepositoryClass(Class<R> repositoryClass) {
        checkThat(value(repositoryClass).notNull().because("Repository class cannot be null"));
        this.repositoryClass = repositoryClass;
    }

    @Override
    public R newRepository() {
        try {
            return repositoryClass.newInstance();
        } catch (Exception e) {
            throw new PousseCafeException("Unable to instantiate repository", e);
        }
    }

    @Override
    public F newFactory() {
        try {
            return factoryClass.newInstance();
        } catch (Exception e) {
            throw new PousseCafeException("Unable to instantiate factory", e);
        }
    }

}
