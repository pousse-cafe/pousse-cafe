package poussecafe.storable;

import java.util.function.Function;
import poussecafe.domain.Entity;

/**
 * @param <J> Stored key type
 * @param <U> Stored value type
 * @param <K> Property key type
 * @param <E> Property value type
 */
public class AdaptedEntityMapPropertyBuilder<J, U extends IdentifiedStorableData<K>, K, E extends Entity<K, ?>> {

    AdaptedEntityMapPropertyBuilder(Class<E> entityClass, Function<J, K> keyAdapter) {
        this.entityClass = entityClass;
        this.keyAdapter = keyAdapter;
    }

    private Class<E> entityClass;

    private Function<J, K> keyAdapter;

    public AdaptedReadOnlyEntityMapPropertyBuilder<J, U, K, E> read() {
        return new AdaptedReadOnlyEntityMapPropertyBuilder<>(entityClass, keyAdapter);
    }
}
