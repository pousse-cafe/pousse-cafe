package poussecafe.attribute;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @param <U> Stored type
 * @param <T> Attribute type
 */
public class AdaptedReadOnlyNumberAttributeBuilder<U, T extends Number> {

    AdaptedReadOnlyNumberAttributeBuilder(Supplier<T> getter) {
        this.getter = getter;
    }

    private Supplier<T> getter;

    public AdaptingReadWriteNumberAttributeBuilder<U, T> adaptOnRead(Function<T, U> adapter) {
        Objects.requireNonNull(adapter);
        return new AdaptingReadWriteNumberAttributeBuilder<>(getter, adapter);
    }
}
