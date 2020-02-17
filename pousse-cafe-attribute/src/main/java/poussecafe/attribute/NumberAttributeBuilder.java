package poussecafe.attribute;

import java.util.function.Supplier;

public class NumberAttributeBuilder<T extends Number> {

    /**
     * @deprecated use read instead.
     */
    @Deprecated(since = "0.18.0")
    public ReadOnlyNumberAttributeBuilder<T> get(Supplier<T> getter) {
        return read(getter);
    }

    public ReadOnlyNumberAttributeBuilder<T> read(Supplier<T> getter) {
        return new ReadOnlyNumberAttributeBuilder<>(getter);
    }
}
