package poussecafe.attribute.entity;

import java.util.function.Function;
import poussecafe.domain.Entity;
import poussecafe.domain.EntityAttributes;

/**
 * @param <J> Stored id type
 * @param <U> Stored value type
 * @param <K> Attribute id type
 * @param <E> Attribute value type
 */
public class AdaptedEntityMapAttributeBuilder<J, U extends EntityAttributes<K>, K, E extends Entity<K, ?>> {

    AdaptedEntityMapAttributeBuilder(Class<E> entityClass, Function<J, K> idAdapter) {
        this.entityClass = entityClass;
        this.idAdapter = idAdapter;
    }

    private Class<E> entityClass;

    private Function<J, K> idAdapter;

    public AdaptedReadOnlyEntityMapAttributeBuilder<J, U, K, E> read() {
        return new AdaptedReadOnlyEntityMapAttributeBuilder<>(entityClass, idAdapter);
    }
}
