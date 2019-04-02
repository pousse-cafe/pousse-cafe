package poussecafe.domain;

import java.util.Objects;
import poussecafe.environment.EntityFactory;
import poussecafe.environment.NewEntityInstanceSpecification;

public abstract class Factory<K, A extends AggregateRoot<K, D>, D extends EntityAttributes<K>> {

    @SuppressWarnings("unchecked")
    public void setEntityClass(Class<?> entityClass) {
        Objects.requireNonNull(entityClass);
        this.entityClass = (Class<A>) entityClass;
    }

    private Class<A> entityClass;

    public Class<A> entityClass() {
        return entityClass;
    }

    protected A newAggregateWithId(K id) {
        Objects.requireNonNull(id);
        A entity = entityFactory.newEntity(new NewEntityInstanceSpecification.Builder<A>()
                .entityClass(entityClass)
                .instantiateData(true)
                .build());
        entity.attributes().identifier().value(id);
        return entity;
    }

    private EntityFactory entityFactory;
}
