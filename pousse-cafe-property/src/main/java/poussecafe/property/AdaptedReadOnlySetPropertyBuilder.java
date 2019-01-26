package poussecafe.property;

import java.util.Objects;
import java.util.function.Function;

/**
 * @param <U> Stored list element type
 * @param <T> Property list element type
 */
public class AdaptedReadOnlySetPropertyBuilder<U, T> {

    AdaptedReadOnlySetPropertyBuilder(Function<U, T> adapter) {
        this.adapter = adapter;
    }

    private Function<U, T> adapter;

    public AdaptingReadWriteSetPropertyBuilder<U, T> adaptOnSet(Function<T, U> adapter) {
        Objects.requireNonNull(adapter);
        return new AdaptingReadWriteSetPropertyBuilder<>(this.adapter, adapter);
    }
}
