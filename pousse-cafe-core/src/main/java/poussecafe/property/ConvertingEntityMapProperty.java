package poussecafe.property;

import java.util.Map;
import poussecafe.domain.Entity;
import poussecafe.domain.EntityData;

import static poussecafe.check.Checks.checkThatValue;

public abstract class ConvertingEntityMapProperty<L, F extends EntityData<K>, K, E extends Entity<K, ?>> implements
        EntityMapProperty<K, E> {

    public ConvertingEntityMapProperty(Map<L, F> data, Class<E> primitiveClass) {
        checkThatValue(data).notNull();
        this.data = data;

        checkThatValue(primitiveClass).notNull();
        this.primitiveClass = primitiveClass;
    }

    private Map<L, F> data;

    private Class<E> primitiveClass;

    @Override
    public MapProperty<K, E> inContextOf(Entity<?, ?> primitive) {
        checkThatValue(primitive).notNull();

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
