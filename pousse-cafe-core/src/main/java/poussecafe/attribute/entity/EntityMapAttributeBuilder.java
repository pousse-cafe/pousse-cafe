package poussecafe.attribute.entity;

import poussecafe.domain.Entity;
import poussecafe.domain.EntityAttributes;

public class EntityMapAttributeBuilder<K, E extends Entity<K, ?>> {

    EntityMapAttributeBuilder(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    private Class<E> entityClass;

    public <J, U extends EntityAttributes<K>> AdaptingEntityMapAttributeBuilder<J, U, K, E> from(Class<J> storedKeyType, Class<U> storedValueType) {
        return new AdaptingEntityMapAttributeBuilder<>(entityClass);
    }
}
