package poussecafe.attribute;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @param <U> Stored type
 * @param <T> Attribute type
 */
public class AdaptingReadOnlyOptionalAttributeBuilder<U, T> {

    AdaptingReadOnlyOptionalAttributeBuilder(Function<U, T> adapter) {
        this.adapter = adapter;
    }

    private Function<U, T> adapter;

    public AdaptedReadOnlyOptionalAttributeBuilder<U, T> read(Supplier<U> getter) {
        Objects.requireNonNull(getter);
        return new AdaptedReadOnlyOptionalAttributeBuilder<>(() -> {
            U value = getter.get();
            if(value != null) {
                return adapter.apply(value);
            } else {
                return null;
            }
        });
    }
}
