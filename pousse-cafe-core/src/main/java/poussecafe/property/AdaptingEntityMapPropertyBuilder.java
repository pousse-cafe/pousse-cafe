package poussecafe.property;

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
public class AdaptingEntityMapPropertyBuilder<J, U extends EntityData<K>, K, E extends Entity<K, ?>> {

    AdaptingEntityMapPropertyBuilder(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    private Class<E> entityClass;

    public AdaptedReadOnlyEntityMapPropertyBuilder<J, U, K, E> adaptOnGet(Function<J, K> keyAdapter) {
        Objects.requireNonNull(keyAdapter);
        return new AdaptedReadOnlyEntityMapPropertyBuilder<>(entityClass, keyAdapter);
    }
}
