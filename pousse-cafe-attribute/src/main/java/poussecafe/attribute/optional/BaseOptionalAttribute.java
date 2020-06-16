package poussecafe.attribute.optional;

import java.util.Objects;
import java.util.Optional;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.OptionalAttribute;

abstract class BaseOptionalAttribute<T> implements OptionalAttribute<T> {

    @Override
    public Optional<T> value() {
        @SuppressWarnings({"removal", "java:S1874"})
        T nullableValue = nullableValue();
        return Optional.ofNullable(nullableValue);
    }

    @SuppressWarnings({"removal", "java:S1874"})
    @Override
    public void value(Optional<T> value) {
        Objects.requireNonNull(value);
        optionalValue(value.orElse(null));
    }

    @SuppressWarnings("java:S1874")
    @Override
    public void nonOptionalValueOf(Attribute<T> property) {
        nonOptionalValue(property.value());
    }

    @SuppressWarnings("java:S1874")
    @Override
    public void nonOptionalValue(T value) {
        value(Optional.of(value));
    }
}
