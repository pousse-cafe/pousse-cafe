package poussecafe.property;

import java.util.Objects;
import java.util.function.Function;

/**
 * @param <U> Stored type
 * @param <T> Property type
 */
public class AdaptingOptionalPropertyBuilder<U, T> {

    AdaptingOptionalPropertyBuilder() {

    }

    public AdaptingReadOnlyOptionalPropertyBuilder<U, T> adapt(Function<U, T> adapter) {
        Objects.requireNonNull(adapter);
        return new AdaptingReadOnlyOptionalPropertyBuilder<>(adapter);
    }
}
