package poussecafe.environment;

import java.util.Objects;
import java.util.function.Supplier;
import poussecafe.domain.Entity;
import poussecafe.runtime.MessageValidator;
import poussecafe.storage.Storage;
import poussecafe.util.ReflectionUtils;

public class EntityFactory {

    static class Builder {

        private EntityFactory entityFactory = new EntityFactory();

        Builder environment(Environment environment) {
            entityFactory.environment = environment;
            return this;
        }

        Builder messageFactory(MessageFactory messageFactory) {
            entityFactory.messageFactory = messageFactory;
            return this;
        }

        Builder messageValidator(MessageValidator messageValidator) {
            entityFactory.messageValidator = messageValidator;
            return this;
        }

        EntityFactory build() {
            Objects.requireNonNull(entityFactory.environment);
            Objects.requireNonNull(entityFactory.messageFactory);
            Objects.requireNonNull(entityFactory.messageValidator);
            return entityFactory;
        }
    }

    private EntityFactory() {

    }

    private Environment environment;

    public <E extends Entity<?, ?>> E newEntity(NewEntityInstanceSpecification<E> specification) {
        Class<E> entityClass = specification.entityClass();
        E entity = ReflectionUtils.newInstance(entityClass);

        entity.entityFactory(this);
        entity.messageFactory(messageFactory);
        entity.messageValidator(messageValidator);

        if(specification.existingData() != null) {
            entity.attributes(specification.existingData());
        }

        Storage storage = environment.storageOfEntity(entityClass);
        if(storage != null) {
            entity.storage(storage);
            entity.messageCollection(storage.getMessageSendingPolicy().newMessageCollection());
        }

        if(environment.entityImplementations.containsKey(entityClass)) {
            Object data = null;
            if(specification.instantiateData()) {
                data = supplyEntityDataImplementation(entityClass);
                entity.attributes(data);
            }
        }

        return entity;
    }

    private MessageFactory messageFactory;

    private MessageValidator messageValidator;

    private Object supplyEntityDataImplementation(Class<?> entityClass) {
        Supplier<?> factory = environment.entityDataFactory(entityClass);
        return factory.get();
    }
}
