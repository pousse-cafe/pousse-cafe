package poussecafe.property;

import poussecafe.domain.Entity;
import poussecafe.domain.EntityData;

import static poussecafe.check.Checks.checkThatValue;

public abstract class EntityPropertyData<D extends EntityData<?>, E extends Entity<?, D>> implements EntityProperty<E> {

    public EntityPropertyData(Class<E> primitiveClass) {
        checkThatValue(primitiveClass).notNull();
        this.primitiveClass = primitiveClass;
    }

    private Class<E> primitiveClass;

    @Override
    public Property<E> inContextOf(Entity<?, ?> primitive) {
        return new Property<E>() {
            @Override
            public E get() {
                E entity = primitive.newEntity(primitiveClass);
                entity.setData(EntityPropertyData.this.getData());
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
    public E newInContextOf(Entity<?, ?> primitive) {
        return primitive.newEntity(primitiveClass);
    }
}
