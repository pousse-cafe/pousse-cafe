package poussecafe.attribute;

import java.util.Objects;
import java.util.function.Function;

/**
 * @param <U> Stored list element type
 * @param <T> Attribute list element type
 */
public class AdaptedReadOnlyListAttributeBuilder<U, T> {

    AdaptedReadOnlyListAttributeBuilder(Function<U, T> adapter) {
        this.adapter = adapter;
    }

    private Function<U, T> adapter;

    public AdaptingReadWriteListAttributeBuilder<U, T> adaptOnSet(Function<T, U> adapter) {
        Objects.requireNonNull(adapter);
        return new AdaptingReadWriteListAttributeBuilder<>(this.adapter, adapter);
    }
}
