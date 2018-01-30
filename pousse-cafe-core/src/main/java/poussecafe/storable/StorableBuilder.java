package poussecafe.storable;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public abstract class StorableBuilder<T extends Storable<?>> extends Primitive {

    protected StorableBuilder(T initialStorable) {
        checkThat(value(initialStorable).notNull().because("Storable cannot be initially null"));
        this.storable = initialStorable;
    }

    private T storable;

    protected T storable() {
        return storable;
    }

    void setData(Object data) {
        storable.setData(data);
    }

    @SuppressWarnings("unchecked")
    public Class<T> getStorableClass() {
        return (Class<T>) storable.getClass();
    }

    public T build() {
        T storable = this.storable;
        checkStorable(storable);
        this.storable = null;
        return storable;
    }

    protected abstract void checkStorable(T storable);
}
