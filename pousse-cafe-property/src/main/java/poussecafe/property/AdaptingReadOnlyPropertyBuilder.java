package poussecafe.property;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @param <U> Stored type
 * @param <T> Property type
 */
public class AdaptingReadOnlyPropertyBuilder<U, T> {

    AdaptingReadOnlyPropertyBuilder(Function<U, T> adapter) {
        this.adapter = adapter;
    }

    private Function<U, T> adapter;

    public AdaptedReadOnlyPropertyBuilder<U, T> get(Supplier<U> getter) {
        Objects.requireNonNull(getter);
        return new AdaptedReadOnlyPropertyBuilder<>(() -> adapter.apply(getter.get()));
    }
}
