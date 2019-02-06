package poussecafe.property;

import java.util.Map;
import java.util.function.Function;
import poussecafe.domain.Entity;
import poussecafe.domain.EntityAttributes;

import java.util.Objects;

/**
 * @param <J> Stored key type
 * @param <U> Stored value type
 * @param <K> Attribute key type
 * @param <E> Attribute value type
 */
public class AdaptedReadOnlyEntityMapAttributeBuilder<J, U extends EntityAttributes<K>, K, E extends Entity<K, ?>> {

    AdaptedReadOnlyEntityMapAttributeBuilder(Class<E> entityClass, Function<J, K> keyAdapter) {
        this.entityClass = entityClass;
        this.keyAdapter = keyAdapter;
    }

    private Class<E> entityClass;

    private Function<J, K> keyAdapter;

    public EntityMapAttribute<K, E> build(Map<J, U> map) {
        return new ConvertingEntityMapAttribute<J, U, K, E>(map, entityClass) {

            @Override
            protected J convertToKey(K from) {
                throw new UnsupportedOperationException();
            }

            @Override
            protected K convertFromKey(J from) {
                return keyAdapter.apply(from);
            }
        };
    }

    public AdaptingReadWriteEntityMapAttributeBuilder<J, U, K, E> adaptOnSet(Function<K, J> keyAdapter) {
        Objects.requireNonNull(keyAdapter);
        return new AdaptingReadWriteEntityMapAttributeBuilder<>(entityClass, this.keyAdapter, keyAdapter);
    }
}
