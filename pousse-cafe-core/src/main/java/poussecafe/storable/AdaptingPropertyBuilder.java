package poussecafe.storable;

import java.util.function.Function;

import static poussecafe.check.Checks.checkThatValue;

/**
 * @param <U> Stored type
 * @param <T> Property type
 */
public class AdaptingPropertyBuilder<U, T> {

    AdaptingPropertyBuilder() {

    }

    public AdaptingReadOnlyPropertyBuilder<U, T> adapt(Function<U, T> adapter) {
        checkThatValue(adapter).notNull();
        return new AdaptingReadOnlyPropertyBuilder<>(adapter);
    }
}
