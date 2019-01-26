package poussecafe.property;

import java.util.Objects;
import java.util.function.Function;

/**
 * @param <U> Stored type
 * @param <T> Property type
 */
public class AdaptingNumberPropertyBuilder<U, T> {

    AdaptingNumberPropertyBuilder() {

    }

    public AdaptingReadOnlyOptionalPropertyBuilder<U, T> adapt(Function<U, T> adapter) {
        Objects.requireNonNull(adapter);
        return new AdaptingReadOnlyOptionalPropertyBuilder<>(adapter);
    }
}
