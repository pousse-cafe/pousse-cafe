package poussecafe.attribute;

import java.util.Objects;
import java.util.function.Function;

/**
 * @param <U> Stored type
 * @param <T> Attribute type
 */
public class AdaptingAttributeBuilder<U, T> {

    AdaptingAttributeBuilder() {

    }

    public AdaptingReadOnlyAttributeBuilder<U, T> adapt(Function<U, T> adapter) {
        Objects.requireNonNull(adapter);
        return new AdaptingReadOnlyAttributeBuilder<>(adapter);
    }
}
