package poussecafe.property;

import java.util.function.Supplier;

public class NumberPropertyBuilder<T extends Number> {

    public ReadOnlyNumberPropertyBuilder<T> get(Supplier<T> getter) {
        return new ReadOnlyNumberPropertyBuilder<>(getter);
    }
}
