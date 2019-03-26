package poussecafe.attribute;

import java.util.Objects;
import java.util.Optional;

public abstract class OptionalAttribute<T> implements Attribute<Optional<T>> {

    @Override
    public Optional<T> value() {
        T nullableValue = nullableValue();
        return Optional.ofNullable(nullableValue);
    }

    public abstract T nullableValue();

    @Override
    public void value(Optional<T> value) {
        Objects.requireNonNull(value);
        optionalValue(value.orElse(null));
    }

    public abstract void optionalValue(T nullableValue);

    public void nonOptionalValueOf(Attribute<T> property) {
        nonOptionalValue(property.value());
    }

    public void nonOptionalValue(T value) {
        value(Optional.of(value));
    }
}
