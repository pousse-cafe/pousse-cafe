package poussecafe.property;

import java.util.function.Function;
import java.util.function.Supplier;

import static poussecafe.check.Checks.checkThatValue;

/**
 * @param <U> Stored type
 * @param <T> Property type
 */
public class AdaptedReadOnlyPropertyBuilder<U, T> {

    AdaptedReadOnlyPropertyBuilder(Supplier<T> getter) {
        this.getter = getter;
    }

    private Supplier<T> getter;

    public Property<T> build() {
        return new ReadOnlyPropertyBuilder<>(getter).build();
    }

    public AdaptingReadWritePropertyBuilder<U, T> adapt(Function<T, U> adapter) {
        checkThatValue(adapter).notNull();
        return new AdaptingReadWritePropertyBuilder<>(getter, adapter);
    }
}
