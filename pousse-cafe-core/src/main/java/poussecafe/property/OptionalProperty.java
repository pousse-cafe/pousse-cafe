package poussecafe.property;

import java.util.Optional;

public abstract class OptionalProperty<T> implements Property<Optional<T>> {

    @Override
    public Optional<T> get() {
        T nullableValue = getNullable();
        return Optional.ofNullable(nullableValue);
    }

    public abstract T getNullable();

    @Override
    public void set(Optional<T> value) {
        setNullable(value.orElse(null));
    }

    public abstract void setNullable(T nullableValue);
}
