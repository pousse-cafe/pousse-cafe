package poussecafe.property;

import java.util.Map;
import poussecafe.domain.Entity;
import poussecafe.domain.EntityData;

import java.util.Objects;

public abstract class ConvertingEntityMapProperty<L, F extends EntityData<K>, K, E extends Entity<K, ?>> implements
        EntityMapProperty<K, E> {

    public ConvertingEntityMapProperty(Map<L, F> data, Class<E> primitiveClass) {
        Objects.requireNonNull(data);
        this.data = data;

        Objects.requireNonNull(primitiveClass);
        this.primitiveClass = primitiveClass;
    }

    private Map<L, F> data;

    private Class<E> primitiveClass;

    @Override
    public MapProperty<K, E> inContextOf(Entity<?, ?> primitive) {
        Objects.requireNonNull(primitive);

        return new ConvertingMapProperty<L, F, K, E>(data) {
            @Override
            protected K convertFromKey(L from) {
                return ConvertingEntityMapProperty.this.convertFromKey(from);
            }

            @Override
            protected E convertFromValue(F from) {
                E entity = primitive.newEntity(primitiveClass);
                entity.setData(from);
                return entity;
            }

            @Override
            protected L convertToKey(K from) {
                return ConvertingEntityMapProperty.this.convertToKey(from);
            }

            @SuppressWarnings("unchecked")
            @Override
            protected F convertToValue(E from) {
                return (F) from.data();
            }
        };
    }

    protected abstract L convertToKey(K from);

    protected abstract K convertFromKey(L from);

    @Override
    public E newInContextOf(Entity<?, ?> primitive) {
        return primitive.newEntity(primitiveClass);
    }
}
