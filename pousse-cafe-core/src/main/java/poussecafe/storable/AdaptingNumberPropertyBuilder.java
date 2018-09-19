package poussecafe.storable;

import java.util.function.Function;

import static poussecafe.check.Checks.checkThatValue;

/**
 * @param <U> Stored type
 * @param <T> Property type
 */
public class AdaptingNumberPropertyBuilder<U, T> {

    AdaptingNumberPropertyBuilder() {

    }

    public AdaptingReadOnlyOptionalPropertyBuilder<U, T> adapt(Function<U, T> adapter) {
        checkThatValue(adapter).notNull();
        return new AdaptingReadOnlyOptionalPropertyBuilder<>(adapter);
    }
}
