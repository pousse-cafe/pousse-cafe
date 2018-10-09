package poussecafe.domain;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public abstract class Factory<K, A extends AggregateRoot<K, D>, D extends EntityData<K>>
        extends Component {

    @SuppressWarnings("unchecked")
    void setEntityClass(Class<?> entityClass) {
        checkThat(value(entityClass).notNull().because("Entity class cannot be null"));
        this.entityClass = (Class<A>) entityClass;
    }

    private Class<A> entityClass;

    protected A newAggregateWithKey(K key) {
        checkThat(value(key).notNull().because("Key cannot be null"));
        A entity = newComponent(entityClass);
        entity.setKey(key);
        return entity;
    }
}
