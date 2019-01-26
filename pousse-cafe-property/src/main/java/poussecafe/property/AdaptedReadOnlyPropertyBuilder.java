package poussecafe.property;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

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
        Objects.requireNonNull(adapter);
        return new AdaptingReadWritePropertyBuilder<>(getter, adapter);
    }
}
