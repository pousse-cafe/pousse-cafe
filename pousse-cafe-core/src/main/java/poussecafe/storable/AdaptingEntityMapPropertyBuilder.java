package poussecafe.storable;

import java.util.function.Function;
import poussecafe.domain.Entity;

import static poussecafe.check.Checks.checkThatValue;

/**
 * @param <J> Stored key type
 * @param <U> Stored value type
 * @param <K> Property key type
 * @param <E> Property value type
 */
public class AdaptingEntityMapPropertyBuilder<J, U extends IdentifiedStorableData<K>, K, E extends Entity<K, ?>> {

    AdaptingEntityMapPropertyBuilder(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    private Class<E> entityClass;

    public AdaptedEntityMapPropertyBuilder<J, U, K, E> adapt(Function<J, K> keyAdapter) {
        checkThatValue(keyAdapter).notNull();
        return new AdaptedEntityMapPropertyBuilder<>(entityClass, keyAdapter);
    }
}
