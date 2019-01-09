package poussecafe.domain;

import java.util.Objects;
import java.util.function.Supplier;
import poussecafe.context.Environment;
import poussecafe.exception.PousseCafeException;
import poussecafe.messaging.Message;
import poussecafe.storage.Storage;

public class ComponentFactory {

    public <E extends Entity<?, ?>> E newEntity(EntitySpecification<E> specification) {
        Class<E> entityClass = specification.getPrimitiveClass();
        E entity = newInstance(entityClass);

        entity.setComponentFactory(this);

        if(specification.getExistingData() != null) {
            entity.setData(specification.getExistingData());
        }

        Storage storage = environment.getStorage(entityClass);
        if(storage != null) {
            entity.storage(storage);
            entity.messageCollection(storage.getMessageSendingPolicy().newMessageCollection());
        }

        if(environment.hasStorageImplementation(entityClass)) {
            Object data = null;
            if(specification.isWithData()) {
                data = supplyEntityDataImplementation(entityClass);
                entity.setData(data);
            }
        }

        return entity;
    }

    private <T> T newInstance(Class<T> componentClass) {
        try {
            return componentClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new PousseCafeException("Unable to instantiate entity", e);
        }
    }

    private Environment environment;

    public void setEnvironment(Environment environment) {
        Objects.requireNonNull(environment);
        this.environment = environment;
    }

    private Object supplyEntityDataImplementation(Class<?> entityClass) {
        Supplier<?> factory = environment.getEntityDataFactory(entityClass);
        return factory.get();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Repository newRepository(Class<? extends Repository> repositoryClass) {
        Repository repository = newInstance(repositoryClass);
        Class<?> entityClass = environment.getEntityClass(repositoryClass);
        repository.setEntityClass(entityClass);
        repository.setDataAccess(supplyDataAccessImplementation(entityClass));
        return repository;
    }

    private Object supplyDataAccessImplementation(Class<?> entityClass) {
        Supplier<?> factory = environment.getEntityDataAccessFactory(entityClass);
        return factory.get();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Factory newFactory(Class<? extends Factory> factoryClass) {
        Factory factory = newInstance(factoryClass);
        Class<?> entityClass = environment.getEntityClass(factoryClass);
        factory.setEntityClass(entityClass);
        return factory;
    }

    @SuppressWarnings("unchecked")
    public <T extends Message> T newMessage(Class<T> primitiveClass) {
        return (T) supplyMessageImplementation(primitiveClass);
    }

    private Object supplyMessageImplementation(Class<?> messageClass) {
        return newInstance(environment.getMessageImplementationClass(messageClass));
    }
}
