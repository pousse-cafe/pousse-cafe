package poussecafe.property;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import poussecafe.domain.Entity;
import poussecafe.domain.EntityData;

/**
 * @param <J> Stored key type
 * @param <U> Stored value type
 * @param <K> Property key type
 * @param <E> Property value type
 */
public class AdaptingReadWriteEntityMapPropertyBuilder<J, U extends EntityData<K>, K, E extends Entity<K, ?>> {

    AdaptingReadWriteEntityMapPropertyBuilder(
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

    public AdaptedReadWriteEntityMapPropertyBuilder<J, U, K, E> withMap(Map<J, U> map) {
        Objects.requireNonNull(map);
        return new AdaptedReadWriteEntityMapPropertyBuilder<>(entityClass, readKeyAdapter, writeKeyAdapter, map);
    }
}
