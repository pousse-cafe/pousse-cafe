package poussecafe.attribute;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @param <U> Stored type
 * @param <T> Attribute type
 */
public class AdaptedReadOnlyOptionalAttributeBuilder<U, T> {

    AdaptedReadOnlyOptionalAttributeBuilder(Supplier<T> getter) {
        this.getter = getter;
    }

    private Supplier<T> getter;

    public AdaptingReadWriteOptionalAttributeBuilder<U, T> adapt(Function<T, U> adapter) {
        Objects.requireNonNull(adapter);
        return new AdaptingReadWriteOptionalAttributeBuilder<>(getter, adapter);
    }
}
