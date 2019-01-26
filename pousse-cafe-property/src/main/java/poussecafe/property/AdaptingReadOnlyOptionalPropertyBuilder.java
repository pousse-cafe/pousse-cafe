package poussecafe.property;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @param <U> Stored type
 * @param <T> Property type
 */
public class AdaptingReadOnlyOptionalPropertyBuilder<U, T> {

    AdaptingReadOnlyOptionalPropertyBuilder(Function<U, T> adapter) {
        this.adapter = adapter;
    }

    private Function<U, T> adapter;

    public AdaptedReadOnlyOptionalPropertyBuilder<U, T> get(Supplier<U> getter) {
        Objects.requireNonNull(getter);
        return new AdaptedReadOnlyOptionalPropertyBuilder<>(() -> {
            U value = getter.get();
            if(value != null) {
                return adapter.apply(value);
            } else {
                return null;
            }
        });
    }
}
