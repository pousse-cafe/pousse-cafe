package poussecafe.attribute.entity;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import poussecafe.attribute.adapters.DataAdapter;
import poussecafe.attribute.adapters.DataAdapters;
import poussecafe.domain.Entity;
import poussecafe.domain.EntityAttributes;

/**
 * @param <J> Stored id type
 * @param <U> Stored value type
 * @param <K> Attribute id type
 * @param <E> Attribute value type
 */
public class AdaptingEntityMapAttributeBuilder<J, U extends EntityAttributes<K>, K, E extends Entity<K, ?>> {

    AdaptingEntityMapAttributeBuilder(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    private Class<E> entityClass;

    public AdaptedReadOnlyEntityMapAttributeBuilder<J, U, K, E> adaptKeyOnRead(Function<J, K> idAdapter) {
        Objects.requireNonNull(idAdapter);
        return new AdaptedReadOnlyEntityMapAttributeBuilder<>(entityClass, idAdapter);
    }

    public AdaptingReadWriteEntityMapAttributeBuilder<J, U, K, E> usingKeyDataAdapter(DataAdapter<J, K> keyDataAdapter) {
        Objects.requireNonNull(keyDataAdapter);
        return new AdaptingReadWriteEntityMapAttributeBuilder<>(entityClass, keyDataAdapter);
    }

    public AdaptedReadWriteEntityMapAttributeBuilder<K, U, K, E> withMap(Map<K, U> map) {
        Objects.requireNonNull(map);
        return new AdaptedReadWriteEntityMapAttributeBuilder<>(entityClass, DataAdapters.identity(), map);
    }
}
