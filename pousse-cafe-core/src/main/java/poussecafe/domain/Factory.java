package poussecafe.domain;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public abstract class Factory<K, A extends AggregateRoot<K, D>, D extends EntityAttributes<K>> {

    @SuppressWarnings("unchecked")
    public void setEntityClass(Class<?> entityClass) {
        checkThat(value(entityClass).notNull().because("Entity class cannot be null"));
        this.entityClass = (Class<A>) entityClass;
    }

    private Class<A> entityClass;

    public Class<A> entityClass() {
        return entityClass;
    }

    protected A newAggregateWithKey(K key) {
        checkThat(value(key).notNull().because("Key cannot be null"));
        A entity = entityFactory.newEntity(new EntitySpecification.Builder<A>()
                .withComponentClass(entityClass)
                .withData(true)
                .build());
        entity.attributes().key().value(key);
        return entity;
    }

    private EntityFactory entityFactory;
}
