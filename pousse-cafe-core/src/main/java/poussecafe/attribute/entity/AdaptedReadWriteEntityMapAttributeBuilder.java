package poussecafe.attribute.entity;

import java.util.Map;
import poussecafe.attribute.adapters.DataAdapter;
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
            DataAdapter<J, K> idAdapter,
            Map<J, U> map) {
        this.entityClass = entityClass;
        this.idAdapter = idAdapter;
        this.map = map;
    }

    private Class<E> entityClass;

    private DataAdapter<J, K> idAdapter;

    private Map<J, U> map;

    @SuppressWarnings("unchecked")
    public EntityMapAttribute<K, E> build() {
        return new ConvertingEntityMapAttribute<>(map, entityClass) {
            @Override
            protected K convertFromKey(J from) {
                return idAdapter.adaptGet(from);
            }

            @Override
            protected J convertToKey(K from) {
                return idAdapter.adaptSet(from);
            }
        };
    }
}
