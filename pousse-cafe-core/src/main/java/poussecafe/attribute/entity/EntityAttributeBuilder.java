package poussecafe.attribute.entity;

import java.util.Objects;
import poussecafe.domain.Entity;
import poussecafe.domain.EntityAttributes;

public class EntityAttributeBuilder {

    private EntityAttributeBuilder() {

    }

    public static <K, E extends Entity<K, ?>> EntityMapAttributeBuilder<K, E> entityMap(Class<K> entityKeyClass, Class<E> entityClass) {
        Objects.requireNonNull(entityKeyClass);
        Objects.requireNonNull(entityClass);
        return new EntityMapAttributeBuilder<>(entityClass);
    }

    public static <D extends EntityAttributes<?>, E extends Entity<?, D>, F extends D> EntityAttributeDataBuilder<D, E, F> entity(Class<E> entityClass, Class<F> dataClass) {
        Objects.requireNonNull(entityClass);
        EntityAttributeDataBuilder<D, E, F> builder = new EntityAttributeDataBuilder<>();
        builder.entityClass = entityClass;
        builder.dataClass = dataClass;
        return builder;
    }
}
