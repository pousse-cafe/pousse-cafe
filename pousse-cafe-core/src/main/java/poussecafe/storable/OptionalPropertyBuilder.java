package poussecafe.storable;

import java.util.function.Supplier;

public class OptionalPropertyBuilder<T> {

    public ReadOnlyOptionalPropertyBuilder<T> get(Supplier<T> getter) {
        return new ReadOnlyOptionalPropertyBuilder<>(getter);
    }

    public <U> AdaptingOptionalPropertyBuilder<U, T> from(Class<U> storedType) {
        return new AdaptingOptionalPropertyBuilder<>();
    }
}
