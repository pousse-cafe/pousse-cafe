package poussecafe.property;

import java.util.Objects;
import java.util.function.Function;

/**
 * @param <U> Stored list element type
 * @param <T> Property list element type
 */
public class AdaptingListPropertyBuilder<U, T> {

    public AdaptedListPropertyBuilder<U, T> adapt(Function<U, T> adapter) {
        Objects.requireNonNull(adapter);
        return new AdaptedListPropertyBuilder<>(adapter);
    }
}
