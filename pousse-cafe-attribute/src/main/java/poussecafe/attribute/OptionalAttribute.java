package poussecafe.attribute;

import java.util.Objects;
import java.util.Optional;

public interface OptionalAttribute<T> extends Attribute<Optional<T>> {

    /**
     * The default implementation will be dropped as soon as deprecated methods are removed.
     */
    @Override
    default Optional<T> value() {
        T nullableValue = nullableValue();
        return Optional.ofNullable(nullableValue);
    }

    @Deprecated(since = "0.19", forRemoval = true)
    T nullableValue();

    /**
     * The default implementation will be dropped as soon as deprecated methods are removed.
     */
    @Override
    default void value(Optional<T> value) {
        Objects.requireNonNull(value);
        optionalValue(value.orElse(null));
    }

    @Deprecated(since = "0.19", forRemoval = true)
    void optionalValue(T nullableValue);

    @Deprecated(since = "0.19", forRemoval = true)
    default void nonOptionalValueOf(Attribute<T> property) {
        nonOptionalValue(property.value());
    }

    @Deprecated(since = "0.19", forRemoval = true)
    default void nonOptionalValue(T value) {
        value(Optional.of(value));
    }
}
