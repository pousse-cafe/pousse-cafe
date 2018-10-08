package poussecafe.storable;

import java.util.function.Supplier;

import static poussecafe.check.Checks.checkThatValue;

public class OptionalPropertyBuilder<T> {

    public ReadOnlyOptionalPropertyBuilder<T> get(Supplier<T> getter) {
        checkThatValue(getter).notNull();
        return new ReadOnlyOptionalPropertyBuilder<>(getter);
    }

    public <U> AdaptingOptionalPropertyBuilder<U, T> from(Class<U> storedType) {
        return new AdaptingOptionalPropertyBuilder<>();
    }
}
