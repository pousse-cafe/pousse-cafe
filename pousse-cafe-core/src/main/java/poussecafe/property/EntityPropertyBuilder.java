package poussecafe.property;

import java.util.Objects;
import poussecafe.domain.Entity;
import poussecafe.domain.EntityData;

public class EntityPropertyBuilder {

    private EntityPropertyBuilder() {

    }

    public static <K, E extends Entity<K, ?>> EntityMapPropertyBuilder<K, E> entityMap(Class<K> entityKeyClass, Class<E> entityClass) {
        Objects.requireNonNull(entityKeyClass);
        Objects.requireNonNull(entityClass);
        return new EntityMapPropertyBuilder<>(entityClass);
    }

    public static <D extends EntityData<?>, E extends Entity<?, D>, F extends D> EntityPropertyDataBuilder<D, E, F> entity(Class<E> entityClass, Class<F> dataClass) {
        Objects.requireNonNull(entityClass);
        EntityPropertyDataBuilder<D, E, F> builder = new EntityPropertyDataBuilder<>();
        builder.entityClass = entityClass;
        builder.dataClass = dataClass;
        return builder;
    }
}
