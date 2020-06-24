package poussecafe.attribute.entity;

import java.util.Objects;
import poussecafe.attribute.OptionalAttribute;
import poussecafe.domain.Entity;
import poussecafe.domain.EntityAttributes;
import poussecafe.runtime.ActiveAggregate;

public abstract class OptionalEntityAttributeData<D extends EntityAttributes<?>, E extends Entity<?, D>>
implements OptionalEntityAttribute<E> {

    public OptionalEntityAttributeData(Class<E> primitiveClass) {
        Objects.requireNonNull(primitiveClass);
        this.primitiveClass = primitiveClass;
    }

    private Class<E> primitiveClass;

    @Override
    public OptionalAttribute<E> inContextOf(Entity<?, ?> primitive) {
        return this;
    }

    @Override
    public E nullableValue() {
        D nullableData = OptionalEntityAttributeData.this.getData();
        if(nullableData == null) {
            return null;
        } else {
            E entity = newEntity();
            entity.attributes(nullableData);
            return entity;
        }
    }

    @Override
    public void optionalValue(E value) {
        if(value == null) {
            setData(null);
        } else {
            setData(value.attributes());
        }
    }

    protected abstract D getData();

    protected abstract void setData(D data);

    @Override
    public E newInContextOf(Entity<?, ?> primitive) {
        return newEntity();
    }

    @SuppressWarnings("unchecked")
    private E newEntity() {
        return (E) ActiveAggregate.instance().get().newEntity(primitiveClass);
    }
}
