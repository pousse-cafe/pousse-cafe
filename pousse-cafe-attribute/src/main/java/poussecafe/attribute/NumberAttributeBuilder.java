package poussecafe.attribute;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class NumberAttributeBuilder<T extends Number> {

    /**
     * @deprecated use read instead.
     */
    @Deprecated(since = "0.18.0")
    public ExpectingWriter<T> get(Supplier<T> getter) {
        return read(getter);
    }

    public ExpectingWriter<T> read(Supplier<T> getter) {
        return new DefaultNumberAttributeBuilder<>(getter);
    }

    public static interface ExpectingWriter<T extends Number> {

        ExpectingAddOperator<T> write(Consumer<T> setter);
    }

    public static interface ExpectingAddOperator<T extends Number> {

        Complete<T> addOperator(AddOperator<T> addOperator);
    }

    public static interface Complete<T extends Number> {

        NumberAttribute<T> build();
    }
}
