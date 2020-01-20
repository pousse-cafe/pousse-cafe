package poussecafe.attribute.entity;

import java.util.Objects;
import poussecafe.domain.Entity;
import poussecafe.domain.EntityAttributes;

public class EntityAttributeBuilder {

    private EntityAttributeBuilder() {

    }

    public static <K, E extends Entity<K, ?>> EntityMapAttributeBuilder<K, E> entityMap(Class<K> entityIdClass, Class<E> entityClass) {
        Objects.requireNonNull(entityIdClass);
        Objects.requireNonNull(entityClass);
        return new EntityMapAttributeBuilder<>(entityClass);
    }

    public static <D extends EntityAttributes<?>, E extends Entity<?, D>, F extends D> SingleEntityAttributeBuilder<D, E, F> entity(Class<E> entityClass, Class<F> dataClass) {
        Objects.requireNonNull(entityClass);
        SingleEntityAttributeBuilder<D, E, F> builder = new SingleEntityAttributeBuilder<>();
        builder.entityClass = entityClass;
        builder.dataClass = dataClass;
        return builder;
    }

    public static <D extends EntityAttributes<?>, E extends Entity<?, D>, F extends D> OptionalEntityAttributeBuilder<D, E, F> optional(Class<E> entityClass, Class<F> dataClass) {
        Objects.requireNonNull(entityClass);
        OptionalEntityAttributeBuilder<D, E, F> builder = new OptionalEntityAttributeBuilder<>();
        builder.entityClass = entityClass;
        builder.dataClass = dataClass;
        return builder;
    }
}
