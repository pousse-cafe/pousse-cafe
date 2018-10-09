package poussecafe.property;

import poussecafe.domain.Entity;
import poussecafe.domain.EntityData;
import poussecafe.domain.Component;

import static poussecafe.check.Checks.checkThatValue;

public abstract class EntityPropertyData<D extends EntityData<?>, E extends Entity<?, D>> implements EntityProperty<E> {

    public EntityPropertyData(Class<E> primitiveClass) {
        checkThatValue(primitiveClass).notNull();
        this.primitiveClass = primitiveClass;
    }

    private Class<E> primitiveClass;

    @Override
    public Property<E> inContextOf(Component primitive) {
        return new Property<E>() {
            @Override
            public E get() {
                E entity = primitive.newComponent(primitiveClass, EntityPropertyData.this.getData());
                entity.parent(primitive);
                return entity;
            }

            @Override
            public void set(E value) {
                EntityPropertyData.this.setData(value.getData());
            }
        };
    }

    protected abstract D getData();

    protected abstract void setData(D data);

    @Override
    public E newInContextOf(Component primitive) {
        E entity = primitive.newComponent(primitiveClass);
        entity.parent(primitive);
        return entity;
    }
}
