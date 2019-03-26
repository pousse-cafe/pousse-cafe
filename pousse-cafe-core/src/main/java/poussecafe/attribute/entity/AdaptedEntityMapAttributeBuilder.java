package poussecafe.attribute.entity;

import java.util.function.Function;
import poussecafe.domain.Entity;
import poussecafe.domain.EntityAttributes;

/**
 * @param <J> Stored key type
 * @param <U> Stored value type
 * @param <K> Attribute key type
 * @param <E> Attribute value type
 */
public class AdaptedEntityMapAttributeBuilder<J, U extends EntityAttributes<K>, K, E extends Entity<K, ?>> {

    AdaptedEntityMapAttributeBuilder(Class<E> entityClass, Function<J, K> keyAdapter) {
        this.entityClass = entityClass;
        this.keyAdapter = keyAdapter;
    }

    private Class<E> entityClass;

    private Function<J, K> keyAdapter;

    public AdaptedReadOnlyEntityMapAttributeBuilder<J, U, K, E> read() {
        return new AdaptedReadOnlyEntityMapAttributeBuilder<>(entityClass, keyAdapter);
    }
}
