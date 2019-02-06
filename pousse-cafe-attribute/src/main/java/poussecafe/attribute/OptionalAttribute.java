package poussecafe.attribute;

import java.util.Objects;
import java.util.Optional;

public abstract class OptionalAttribute<T> implements Attribute<Optional<T>> {

    @Override
    public Optional<T> value() {
        T nullableValue = getNullable();
        return Optional.ofNullable(nullableValue);
    }

    public abstract T getNullable();

    @Override
    public void value(Optional<T> value) {
        Objects.requireNonNull(value);
        setNullable(value.orElse(null));
    }

    public abstract void setNullable(T nullableValue);

    public void setNonOptionalValueOf(Attribute<T> property) {
        value(Optional.of(property.value()));
    }
}
