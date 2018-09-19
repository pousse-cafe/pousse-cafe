package poussecafe.storable;

import poussecafe.domain.Entity;

public class EntityMapPropertyBuilder<K, E extends Entity<K, ?>> {

    EntityMapPropertyBuilder(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    private Class<E> entityClass;

    public <J, U extends IdentifiedStorableData<K>> AdaptingEntityMapPropertyBuilder<J, U, K, E> from(Class<J> storedKeyType, Class<U> storedValueType) {
        return new AdaptingEntityMapPropertyBuilder<>(entityClass);
    }
}
