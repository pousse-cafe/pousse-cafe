package poussecafe.attribute;

import java.util.Objects;
import java.util.function.Function;

/**
 * @param <U> Stored list element type
 * @param <T> Attribute list element type
 */
public class AdaptingSetAttributeBuilder<U, T> {

    public AdaptedReadOnlySetAttributeBuilder<U, T> adaptOnGet(Function<U, T> adapter) {
        Objects.requireNonNull(adapter);
        return new AdaptedReadOnlySetAttributeBuilder<>(adapter);
    }
}
