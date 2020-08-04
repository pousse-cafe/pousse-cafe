package poussecafe.attribute.entity;

import java.util.Collection;
import poussecafe.domain.Entity;
import poussecafe.domain.EntityAttributes;

public class EntityMapAttributeBuilder<K, E extends Entity<K, ?>> {

    EntityMapAttributeBuilder(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    private Class<E> entityClass;

    public <J, U extends EntityAttributes<K>> AdaptingEntityMapAttributeBuilder<J, U, K, E> entriesStoredAs(
            Class<J> storedIdType, Class<U> storedValueType) {
        return new AdaptingEntityMapAttributeBuilder<>(entityClass);
    }

    public <U extends EntityAttributes<K>> Complete<K, E> withCollection(Collection<U> collection) {
        var builder = new AdaptingEntityCollectionBackedMapAttributeBuilder<U, K, E>(entityClass);
        builder.collection = collection;
        return builder;
    }

    public static interface Complete<K, E extends Entity<K, ?>> {

        EntityMapAttribute<K, E> build();
    }
}
