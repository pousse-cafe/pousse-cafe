package poussecafe.property;

import java.util.Map;
import java.util.function.Function;
import poussecafe.domain.Entity;
import poussecafe.domain.EntityData;

import java.util.Objects;

/**
 * @param <J> Stored key type
 * @param <U> Stored value type
 * @param <K> Property key type
 * @param <E> Property value type
 */
public class AdaptedReadOnlyEntityMapPropertyBuilder<J, U extends EntityData<K>, K, E extends Entity<K, ?>> {

    AdaptedReadOnlyEntityMapPropertyBuilder(Class<E> entityClass, Function<J, K> keyAdapter) {
        this.entityClass = entityClass;
        this.keyAdapter = keyAdapter;
    }

    private Class<E> entityClass;

    private Function<J, K> keyAdapter;

    public EntityMapProperty<K, E> build(Map<J, U> map) {
        return new ConvertingEntityMapProperty<J, U, K, E>(map, entityClass) {

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

    public AdaptingReadWriteEntityMapPropertyBuilder<J, U, K, E> adapt(Function<K, J> keyAdapter) {
        Objects.requireNonNull(keyAdapter);
        return new AdaptingReadWriteEntityMapPropertyBuilder<>(entityClass, this.keyAdapter, keyAdapter);
    }
}
