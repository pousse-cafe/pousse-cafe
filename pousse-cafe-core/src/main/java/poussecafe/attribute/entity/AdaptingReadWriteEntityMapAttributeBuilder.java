package poussecafe.attribute.entity;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import poussecafe.domain.Entity;
import poussecafe.domain.EntityAttributes;

/**
 * @param <J> Stored id type
 * @param <U> Stored value type
 * @param <K> Attribute id type
 * @param <E> Attribute value type
 */
public class AdaptingReadWriteEntityMapAttributeBuilder<J, U extends EntityAttributes<K>, K, E extends Entity<K, ?>> {

    AdaptingReadWriteEntityMapAttributeBuilder(
            Class<E> entityClass,
            Function<J, K> readIdAdapter,
            Function<K, J> writeIdAdapter) {
        this.entityClass = entityClass;
        this.readIdAdapter = readIdAdapter;
        this.writeIdAdapter = writeIdAdapter;
    }

    private Class<E> entityClass;

    private Function<J, K> readIdAdapter;

    private Function<K, J> writeIdAdapter;

    public AdaptedReadWriteEntityMapAttributeBuilder<J, U, K, E> withMap(Map<J, U> map) {
        Objects.requireNonNull(map);
        return new AdaptedReadWriteEntityMapAttributeBuilder<>(entityClass, readIdAdapter, writeIdAdapter, map);
    }
}
