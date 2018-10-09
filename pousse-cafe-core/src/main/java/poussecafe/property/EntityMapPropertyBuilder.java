package poussecafe.property;

import poussecafe.domain.Entity;
import poussecafe.domain.EntityData;

public class EntityMapPropertyBuilder<K, E extends Entity<K, ?>> {

    EntityMapPropertyBuilder(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    private Class<E> entityClass;

    public <J, U extends EntityData<K>> AdaptingEntityMapPropertyBuilder<J, U, K, E> from(Class<J> storedKeyType, Class<U> storedValueType) {
        return new AdaptingEntityMapPropertyBuilder<>(entityClass);
    }
}
