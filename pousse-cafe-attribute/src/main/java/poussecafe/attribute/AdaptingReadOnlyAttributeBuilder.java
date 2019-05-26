package poussecafe.attribute;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @param <U> Stored type
 * @param <T> Attribute type
 */
public class AdaptingReadOnlyAttributeBuilder<U, T> {

    AdaptingReadOnlyAttributeBuilder(Function<U, T> adapter) {
        this.adapter = adapter;
    }

    private Function<U, T> adapter;

    public AdaptedReadOnlyAttributeBuilder<U, T> read(Supplier<U> getter) {
        Objects.requireNonNull(getter);
        return new AdaptedReadOnlyAttributeBuilder<>(() -> adapter.apply(getter.get()));
    }
}
