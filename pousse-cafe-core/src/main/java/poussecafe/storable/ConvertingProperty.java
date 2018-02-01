package poussecafe.storable;

import poussecafe.inmemory.BaseProperty;

public abstract class ConvertingProperty<F, T> extends BaseProperty<T> {

    public ConvertingProperty(Property<F> from, Class<T> toClass) {
        super(toClass);
        this.from = from;
    }

    private Property<F> from;

    @Override
    protected T getValue() {
        F fromValue = from.get();
        if(fromValue == null) {
            return defaultValue();
        } else {
            return convertFrom(fromValue);
        }
    }

    protected abstract T convertFrom(F from);

    @Override
    protected void setValue(T value) {
        from.set(convertTo(value));
    }

    protected abstract F convertTo(T to);
}
