package poussecafe.attribute.entity;

import java.util.Objects;
import poussecafe.attribute.Attribute;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.Entity;
import poussecafe.domain.EntityAttributes;
import poussecafe.runtime.ActiveAggregate;

public abstract class EntityAttributeData<D extends EntityAttributes<?>, E extends Entity<?, D>> implements EntityAttribute<E> {

    public EntityAttributeData(Class<E> primitiveClass) {
        Objects.requireNonNull(primitiveClass);
        this.primitiveClass = primitiveClass;

        root = ActiveAggregate.instance().get();
    }

    private Class<E> primitiveClass;

    @SuppressWarnings("rawtypes")
    private AggregateRoot root;

    @Override
    public Attribute<E> inContextOf(Entity<?, ?> primitive) {
        return this;
    }

    protected abstract D getData();

    protected abstract void setData(D data);

    @Override
    public E newInContextOf(Entity<?, ?> primitive) {
        return newEntity();
    }

    @SuppressWarnings("unchecked")
    private E newEntity() {
        return (E) root.newEntity(primitiveClass);
    }

    @Override
    public E value() {
        E entity = newEntity();
        entity.attributes(getData());
        return entity;
    }

    @Override
    public void value(E value) {
        setData(value.attributes());
    }
}
