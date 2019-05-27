package poussecafe.attribute.entity;

import java.util.Map;
import java.util.Objects;
import poussecafe.attribute.adapters.DataAdapter;
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
            DataAdapter<J, K> idAdapter) {
        this.entityClass = entityClass;
        this.idAdapter = idAdapter;
    }

    private Class<E> entityClass;

    private DataAdapter<J, K> idAdapter;

    public AdaptedReadWriteEntityMapAttributeBuilder<J, U, K, E> withMap(Map<J, U> map) {
        Objects.requireNonNull(map);
        return new AdaptedReadWriteEntityMapAttributeBuilder<>(entityClass, idAdapter, map);
    }
}
