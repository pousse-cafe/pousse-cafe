package poussecafe.storable;

import static poussecafe.check.Checks.checkThatValue;

public abstract class EntityPropertyData<D extends IdentifiedStorableData<?>, E extends IdentifiedStorable<?, D>> implements EntityProperty<E> {

    public EntityPropertyData(Class<E> primitiveClass) {
        checkThatValue(primitiveClass).notNull();
        this.primitiveClass = primitiveClass;
    }

    private Class<E> primitiveClass;

    @Override
    public Property<E> with(Primitive primitive) {
        return new Property<E>() {
            @Override
            public E get() {
                return primitive.newPrimitive(primitiveClass, EntityPropertyData.this.getData());
            }

            @Override
            public void set(E value) {
                EntityPropertyData.this.setData(value.getData());
            }
        };
    }

    protected abstract D getData();

    protected abstract void setData(D data);
}
