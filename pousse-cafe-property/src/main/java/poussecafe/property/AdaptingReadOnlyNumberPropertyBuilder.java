package poussecafe.property;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @param <U> Stored type
 * @param <T> Property type
 */
public class AdaptingReadOnlyNumberPropertyBuilder<U, T> {

    AdaptingReadOnlyNumberPropertyBuilder(Function<U, T> adapter) {
        this.adapter = adapter;
    }

    private Function<U, T> adapter;

    public AdaptedReadOnlyOptionalPropertyBuilder<U, T> get(Supplier<U> getter) {
        Objects.requireNonNull(getter);
        return new AdaptedReadOnlyOptionalPropertyBuilder<>(() -> adapter.apply(getter.get()));
    }
}
