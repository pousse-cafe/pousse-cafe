package poussecafe.attribute;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @param <U> Stored type
 * @param <T> Attribute type
 */
public class AdaptedReadOnlyAttributeBuilder<U, T> {

    AdaptedReadOnlyAttributeBuilder(Supplier<T> getter) {
        this.getter = getter;
    }

    private Supplier<T> getter;

    public AdaptingReadWriteAttributeBuilder<U, T> adapt(Function<T, U> adapter) {
        Objects.requireNonNull(adapter);
        return new AdaptingReadWriteAttributeBuilder<>(getter, adapter);
    }
}
