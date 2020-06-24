package poussecafe.domain;

import poussecafe.environment.EntityFactory;
import poussecafe.environment.NewEntityInstanceSpecification;

import static java.util.Objects.requireNonNull;

public class EntityBuilder<K, D extends EntityAttributes<K>, E extends Entity<K, D>> {

    public EntityBuilder(EntityFactory factory) {
        requireNonNull(factory);
        this.entityFactory = factory;
    }

    private EntityFactory entityFactory;

    public EntityBuilder<K, D, E> withType(Class<E> entityClass) {
        this.entityClass = entityClass;
        return this;
    }

    private Class<E> entityClass;

    public EntityBuilder<K, D, E> withId(K id) {
        this.entityId = id;
        return this;
    }

    private K entityId;

    public E build() {
        E entity = buildWithoutId();
        requireNonNull(entityId);
        entity.attributes().identifier().value(entityId);
        return entity;
    }

    public E buildWithoutId() {
        requireNonNull(entityClass);
        return entityFactory.newEntity(new NewEntityInstanceSpecification.Builder<E>()
                .entityClass(entityClass)
                .instantiateData(true)
                .build());
    }
}
