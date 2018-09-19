package poussecafe.storable;

import java.util.function.Function;
import poussecafe.domain.Entity;

/**
 * @param <J> Stored key type
 * @param <U> Stored value type
 * @param <K> Property key type
 * @param <E> Property value type
 */
public class AdaptingReadWriteEntityMapPropertyBuilder<J, U extends IdentifiedStorableData<K>, K, E extends Entity<K, ?>> {

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

    public AdaptedReadWriteEntityMapPropertyBuilder<J, U, K, E> write() {
        return new AdaptedReadWriteEntityMapPropertyBuilder<>(entityClass, readKeyAdapter, writeKeyAdapter);
    }
}
