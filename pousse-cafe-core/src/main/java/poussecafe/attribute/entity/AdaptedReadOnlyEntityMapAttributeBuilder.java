package poussecafe.attribute.entity;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import poussecafe.attribute.adapters.DataAdapter;
import poussecafe.domain.Entity;
import poussecafe.domain.EntityAttributes;

/**
 * @param <J> Stored id type
 * @param <U> Stored value type
 * @param <K> Attribute id type
 * @param <E> Attribute value type
 */
public class AdaptedReadOnlyEntityMapAttributeBuilder<J, U extends EntityAttributes<K>, K, E extends Entity<K, ?>> {

    AdaptedReadOnlyEntityMapAttributeBuilder(Class<E> entityClass, Function<J, K> idAdapter) {
        this.entityClass = entityClass;
        this.readAdapter = idAdapter;
    }

    private Class<E> entityClass;

    private Function<J, K> readAdapter;

    @SuppressWarnings("unchecked")
    public <G extends Entity<K, U>> EntityMapAttribute<K, G> build(Map<J, U> map) {
        return new ConvertingEntityMapAttribute<>(map, (Class<G>) entityClass) {

            @Override
            protected J convertToKey(K from) {
                throw new UnsupportedOperationException();
            }

            @Override
            protected K convertFromKey(J from) {
                return readAdapter.apply(from);
            }
        };
    }

    public AdaptingReadWriteEntityMapAttributeBuilder<J, U, K, E> adaptKeyOnWrite(Function<K, J> writeAdapter) {
        Objects.requireNonNull(writeAdapter);
        return new AdaptingReadWriteEntityMapAttributeBuilder<>(entityClass, new DataAdapter<>() {

            @Override
            public K adaptGet(J storedValue) {
                return readAdapter.apply(storedValue);
            }

            @Override
            public J adaptSet(K valueToStore) {
                return writeAdapter.apply(valueToStore);
            }
        });
    }
}
