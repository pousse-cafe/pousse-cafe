package poussecafe.attribute.entity;

import java.util.Map;
import java.util.function.Function;
import poussecafe.domain.Entity;
import poussecafe.domain.EntityAttributes;

import java.util.Objects;

/**
 * @param <J> Stored id type
 * @param <U> Stored value type
 * @param <K> Attribute id type
 * @param <E> Attribute value type
 */
public class AdaptedReadOnlyEntityMapAttributeBuilder<J, U extends EntityAttributes<K>, K, E extends Entity<K, ?>> {

    AdaptedReadOnlyEntityMapAttributeBuilder(Class<E> entityClass, Function<J, K> idAdapter) {
        this.entityClass = entityClass;
        this.idAdapter = idAdapter;
    }

    private Class<E> entityClass;

    private Function<J, K> idAdapter;

    public EntityMapAttribute<K, E> build(Map<J, U> map) {
        return new ConvertingEntityMapAttribute<J, U, K, E>(map, entityClass) {

            @Override
            protected J convertToKey(K from) {
                throw new UnsupportedOperationException();
            }

            @Override
            protected K convertFromKey(J from) {
                return idAdapter.apply(from);
            }
        };
    }

    public AdaptingReadWriteEntityMapAttributeBuilder<J, U, K, E> adaptKeyOnWrite(Function<K, J> idAdapter) {
        Objects.requireNonNull(idAdapter);
        return new AdaptingReadWriteEntityMapAttributeBuilder<>(entityClass, this.idAdapter, idAdapter);
    }
}
