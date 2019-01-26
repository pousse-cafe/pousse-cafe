package poussecafe.property;

import java.util.Objects;
import java.util.function.Function;

/**
 * @param <U> Stored list element type
 * @param <T> Property list element type
 */
public class AdaptingSetPropertyBuilder<U, T> {

    public AdaptedReadOnlySetPropertyBuilder<U, T> adaptOnGet(Function<U, T> adapter) {
        Objects.requireNonNull(adapter);
        return new AdaptedReadOnlySetPropertyBuilder<>(adapter);
    }
}
