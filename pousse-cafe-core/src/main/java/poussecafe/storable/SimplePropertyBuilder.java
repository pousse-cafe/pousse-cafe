package poussecafe.storable;

import java.util.function.Supplier;

public class SimplePropertyBuilder<T> {

    public ReadOnlyPropertyBuilder<T> get(Supplier<T> getter) {
        return new ReadOnlyPropertyBuilder<>(getter);
    }

    public <U> AdaptingPropertyBuilder<U, T> from(Class<U> storedType) {
        return new AdaptingPropertyBuilder<>();
    }
}
