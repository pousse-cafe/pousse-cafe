package poussecafe.property;

import java.util.function.Function;
import java.util.function.Supplier;

import static poussecafe.check.Checks.checkThatValue;

/**
 * @param <U> Stored type
 * @param <T> Property type
 */
public class AdaptedReadOnlyNumberPropertyBuilder<U, T> {

    AdaptedReadOnlyNumberPropertyBuilder(Supplier<T> getter) {
        this.getter = getter;
    }

    private Supplier<T> getter;

    public OptionalProperty<T> build() {
        return new ReadOnlyOptionalPropertyBuilder<>(getter).build();
    }

    public AdaptingReadWriteOptionalPropertyBuilder<U, T> adapt(Function<T, U> adapter) {
        checkThatValue(adapter).notNull();
        return new AdaptingReadWriteOptionalPropertyBuilder<>(getter, adapter);
    }
}
