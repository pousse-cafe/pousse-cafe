package poussecafe.attribute.entity;

import java.util.Map;
import java.util.Objects;
import poussecafe.attribute.MapAttribute;
import poussecafe.attribute.map.AdaptingMapAttribute;
import poussecafe.domain.Entity;
import poussecafe.domain.EntityAttributes;

public abstract class ConvertingEntityMapAttribute<L, F extends EntityAttributes<K>, K, E extends Entity<K, ?>> implements
        EntityMapAttribute<K, E> {

    public ConvertingEntityMapAttribute(Map<L, F> data, Class<E> primitiveClass) {
        Objects.requireNonNull(data);
        this.data = data;

        Objects.requireNonNull(primitiveClass);
        this.primitiveClass = primitiveClass;
    }

    private Map<L, F> data;

    private Class<E> primitiveClass;

    @Override
    public MapAttribute<K, E> inContextOf(Entity<?, ?> primitive) {
        Objects.requireNonNull(primitive);

        return new AdaptingMapAttribute<L, F, K, E>(data) {
            @Override
            protected K convertFromKey(L from) {
                return ConvertingEntityMapAttribute.this.convertFromKey(from);
            }

            @Override
            protected E convertFromValue(F from) {
                E entity = primitive.newEntity(primitiveClass);
                entity.attributes(from);
                return entity;
            }

            @Override
            protected L convertToKey(K from) {
                return ConvertingEntityMapAttribute.this.convertToKey(from);
            }

            @SuppressWarnings("unchecked")
            @Override
            protected F convertToValue(E from) {
                return (F) from.attributes();
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
