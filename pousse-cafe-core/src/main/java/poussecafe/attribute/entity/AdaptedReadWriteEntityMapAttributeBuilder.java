package poussecafe.attribute.entity;

import java.util.Map;
import java.util.function.Function;
import poussecafe.domain.Entity;
import poussecafe.domain.EntityAttributes;

/**
 * @param <J> Stored id type
 * @param <U> Stored value type
 * @param <K> Attribute id type
 * @param <E> Attribute value type
 */
public class AdaptedReadWriteEntityMapAttributeBuilder<J, U extends EntityAttributes<K>, K, E extends Entity<K, ?>> {

    AdaptedReadWriteEntityMapAttributeBuilder(
            Class<E> entityClass,
            Function<J, K> readIdAdapter,
            Function<K, J> writeIdAdapter,
            Map<J, U> map) {
        this.entityClass = entityClass;
        this.readIdAdapter = readIdAdapter;
        this.writeIdAdapter = writeIdAdapter;
        this.map = map;
    }

    private Class<E> entityClass;

    private Function<J, K> readIdAdapter;

    private Function<K, J> writeIdAdapter;

    private Map<J, U> map;

    public EntityMapAttribute<K, E> build() {
        return new ConvertingEntityMapAttribute<J, U, K, E>(map, entityClass) {
            @Override
            protected K convertFromKey(J from) {
                return readIdAdapter.apply(from);
            }

            @Override
            protected J convertToKey(K from) {
                return writeIdAdapter.apply(from);
            }
        };
    }
}
