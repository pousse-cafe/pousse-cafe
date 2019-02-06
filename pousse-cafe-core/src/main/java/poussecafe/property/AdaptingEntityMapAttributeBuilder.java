package poussecafe.property;

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
public class AdaptingEntityMapAttributeBuilder<J, U extends EntityAttributes<K>, K, E extends Entity<K, ?>> {

    AdaptingEntityMapAttributeBuilder(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    private Class<E> entityClass;

    public AdaptedReadOnlyEntityMapAttributeBuilder<J, U, K, E> adaptOnGet(Function<J, K> keyAdapter) {
        Objects.requireNonNull(keyAdapter);
        return new AdaptedReadOnlyEntityMapAttributeBuilder<>(entityClass, keyAdapter);
    }
}
