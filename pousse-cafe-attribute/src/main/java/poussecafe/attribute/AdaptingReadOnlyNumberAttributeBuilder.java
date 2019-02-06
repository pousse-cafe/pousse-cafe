package poussecafe.attribute;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @param <U> Stored type
 * @param <T> Attribute type
 */
public class AdaptingReadOnlyNumberAttributeBuilder<U, T> {

    AdaptingReadOnlyNumberAttributeBuilder(Function<U, T> adapter) {
        this.adapter = adapter;
    }

    private Function<U, T> adapter;

    public AdaptedReadOnlyOptionalAttributeBuilder<U, T> get(Supplier<U> getter) {
        Objects.requireNonNull(getter);
        return new AdaptedReadOnlyOptionalAttributeBuilder<>(() -> adapter.apply(getter.get()));
    }
}
