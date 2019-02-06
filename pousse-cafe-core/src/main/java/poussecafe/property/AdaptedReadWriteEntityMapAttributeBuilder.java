package poussecafe.property;

import java.util.Map;
import java.util.function.Function;
import poussecafe.domain.Entity;
import poussecafe.domain.EntityAttributes;

/**
 * @param <J> Stored key type
 * @param <U> Stored value type
 * @param <K> Attribute key type
 * @param <E> Attribute value type
 */
public class AdaptedReadWriteEntityMapAttributeBuilder<J, U extends EntityAttributes<K>, K, E extends Entity<K, ?>> {

    AdaptedReadWriteEntityMapAttributeBuilder(
            Class<E> entityClass,
            Function<J, K> readKeyAdapter,
            Function<K, J> writeKeyAdapter,
            Map<J, U> map) {
        this.entityClass = entityClass;
        this.readKeyAdapter = readKeyAdapter;
        this.writeKeyAdapter = writeKeyAdapter;
        this.map = map;
    }

    private Class<E> entityClass;

    private Function<J, K> readKeyAdapter;

    private Function<K, J> writeKeyAdapter;

    private Map<J, U> map;

    public EntityMapAttribute<K, E> build() {
        return new ConvertingEntityMapAttribute<J, U, K, E>(map, entityClass) {
            @Override
            protected K convertFromKey(J from) {
                return readKeyAdapter.apply(from);
            }

            @Override
            protected J convertToKey(K from) {
                return writeKeyAdapter.apply(from);
            }
        };
    }
}
