package poussecafe.storable;

import poussecafe.storage.memory.BaseProperty;

public abstract class ConvertingProperty<F, T> extends BaseProperty<T> {

    public ConvertingProperty(Property<F> from) {
        this.from = from;
    }

    private Property<F> from;

    @Override
    protected T getValue() {
        F fromValue = from.get();
        return convertFrom(fromValue);
    }

    protected abstract T convertFrom(F from);

    @Override
    protected void setValue(T value) {
        from.set(convertTo(value));
    }

    protected abstract F convertTo(T to);
}
