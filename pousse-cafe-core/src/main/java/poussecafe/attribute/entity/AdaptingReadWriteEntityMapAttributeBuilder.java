package poussecafe.attribute.entity;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import poussecafe.domain.Entity;
import poussecafe.domain.EntityAttributes;

/**
 * @param <J> Stored key type
 * @param <U> Stored value type
 * @param <K> Attribute key type
 * @param <E> Attribute value type
 */
public class AdaptingReadWriteEntityMapAttributeBuilder<J, U extends EntityAttributes<K>, K, E extends Entity<K, ?>> {

    AdaptingReadWriteEntityMapAttributeBuilder(
            Class<E> entityClass,
            Function<J, K> readKeyAdapter,
            Function<K, J> writeKeyAdapter) {
        this.entityClass = entityClass;
        this.readKeyAdapter = readKeyAdapter;
        this.writeKeyAdapter = writeKeyAdapter;
    }

    private Class<E> entityClass;

    private Function<J, K> readKeyAdapter;

    private Function<K, J> writeKeyAdapter;

    public AdaptedReadWriteEntityMapAttributeBuilder<J, U, K, E> withMap(Map<J, U> map) {
        Objects.requireNonNull(map);
        return new AdaptedReadWriteEntityMapAttributeBuilder<>(entityClass, readKeyAdapter, writeKeyAdapter, map);
    }
}
