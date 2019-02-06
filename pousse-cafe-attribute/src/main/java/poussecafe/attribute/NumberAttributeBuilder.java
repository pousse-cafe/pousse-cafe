package poussecafe.attribute;

import java.util.function.Supplier;

public class NumberAttributeBuilder<T extends Number> {

    public ReadOnlyNumberAttributeBuilder<T> get(Supplier<T> getter) {
        return new ReadOnlyNumberAttributeBuilder<>(getter);
    }
}
