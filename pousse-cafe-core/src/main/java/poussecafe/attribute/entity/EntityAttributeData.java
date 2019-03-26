package poussecafe.attribute.entity;

import poussecafe.attribute.Attribute;
import poussecafe.domain.Entity;
import poussecafe.domain.EntityAttributes;

import java.util.Objects;

public abstract class EntityAttributeData<D extends EntityAttributes<?>, E extends Entity<?, D>> implements EntityAttribute<E> {

    public EntityAttributeData(Class<E> primitiveClass) {
        Objects.requireNonNull(primitiveClass);
        this.primitiveClass = primitiveClass;
    }

    private Class<E> primitiveClass;

    @Override
    public Attribute<E> inContextOf(Entity<?, ?> primitive) {
        return new Attribute<E>() {
            @Override
            public E value() {
                E entity = primitive.newEntity(primitiveClass);
                entity.attributes(EntityAttributeData.this.getData());
                return entity;
            }

            @Override
            public void value(E value) {
                EntityAttributeData.this.setData(value.attributes());
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
