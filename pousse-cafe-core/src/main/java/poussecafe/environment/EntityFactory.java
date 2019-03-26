package poussecafe.environment;

import java.util.Objects;
import java.util.function.Supplier;
import poussecafe.domain.Entity;
import poussecafe.storage.Storage;
import poussecafe.util.ReflectionUtils;

public class EntityFactory {

    public static class Builder {

        private EntityFactory entityFactory = new EntityFactory();

        public Builder environment(Environment environment) {
            entityFactory.environment = environment;
            return this;
        }

        public Builder messageFactory(MessageFactory messageFactory) {
            entityFactory.messageFactory = messageFactory;
            return this;
        }

        public EntityFactory build() {
            Objects.requireNonNull(entityFactory.environment);
            Objects.requireNonNull(entityFactory.messageFactory);
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

    private Object supplyEntityDataImplementation(Class<?> entityClass) {
        Supplier<?> factory = environment.entityDataFactory(entityClass);
        return factory.get();
    }
}
