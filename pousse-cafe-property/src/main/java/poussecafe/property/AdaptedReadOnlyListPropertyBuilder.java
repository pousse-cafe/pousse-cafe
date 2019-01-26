package poussecafe.property;

import java.util.Objects;
import java.util.function.Function;

/**
 * @param <U> Stored list element type
 * @param <T> Property list element type
 */
public class AdaptedReadOnlyListPropertyBuilder<U, T> {

    AdaptedReadOnlyListPropertyBuilder(Function<U, T> adapter) {
        this.adapter = adapter;
    }

    private Function<U, T> adapter;

    public AdaptingReadWriteListPropertyBuilder<U, T> adaptOnSet(Function<T, U> adapter) {
        Objects.requireNonNull(adapter);
        return new AdaptingReadWriteListPropertyBuilder<>(this.adapter, adapter);
    }
}
