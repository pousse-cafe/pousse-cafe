package poussecafe.property;

import java.util.Objects;
import java.util.function.Function;

/**
 * @param <U> Stored type
 * @param <T> Property type
 */
public class AdaptingPropertyBuilder<U, T> {

    AdaptingPropertyBuilder() {

    }

    public AdaptingReadOnlyPropertyBuilder<U, T> adapt(Function<U, T> adapter) {
        Objects.requireNonNull(adapter);
        return new AdaptingReadOnlyPropertyBuilder<>(adapter);
    }
}
