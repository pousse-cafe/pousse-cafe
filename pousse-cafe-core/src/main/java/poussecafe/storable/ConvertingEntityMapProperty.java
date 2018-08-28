package poussecafe.storable;

import java.util.HashMap;
import poussecafe.storage.memory.ConvertingMapProperty;

import static poussecafe.check.Checks.checkThatValue;

public abstract class ConvertingEntityMapProperty<L, F extends IdentifiedStorableData<K>, K, E extends IdentifiedStorable<K, ?>> implements
        EntityMapProperty<K, E> {

    public ConvertingEntityMapProperty(HashMap<L, F> data, Class<E> primitiveClass) {
        checkThatValue(data).notNull();
        this.data = data;

        checkThatValue(primitiveClass).notNull();
        this.primitiveClass = primitiveClass;
    }

    private HashMap<L, F> data;

    private Class<E> primitiveClass;

    @Override
    public MapProperty<K, E> inContextOf(Primitive primitive) {
        checkThatValue(primitive).notNull();

        return new ConvertingMapProperty<L, F, K, E>(data) {
            @Override
            protected K convertFromKey(L from) {
                return ConvertingEntityMapProperty.this.convertFromKey(from);
            }

            @Override
            protected E convertFromValue(F from) {
                E entity = primitive.newPrimitive(primitiveClass, from);
                entity.parent(primitive);
                return entity;
            }

            @Override
            protected L convertToKey(K from) {
                return ConvertingEntityMapProperty.this.convertToKey(from);
            }

            @SuppressWarnings("unchecked")
            @Override
            protected F convertToValue(E from) {
                return (F) from.getData();
            }
        };
    }

    protected abstract L convertToKey(K from);

    protected abstract K convertFromKey(L from);

    @Override
    public E newInContextOf(Primitive primitive) {
        E entity = primitive.newPrimitive(primitiveClass);
        entity.parent(primitive);
        return entity;
    }
}
