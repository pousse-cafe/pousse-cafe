package poussecafe.property;

import java.util.Objects;
import java.util.function.Function;

/**
 * @param <U> Stored list element type
 * @param <T> Property list element type
 */
public class AdaptingListPropertyBuilder<U, T> {

    public AdaptedReadOnlyListPropertyBuilder<U, T> adaptOnGet(Function<U, T> adapter) {
        Objects.requireNonNull(adapter);
        return new AdaptedReadOnlyListPropertyBuilder<>(adapter);
    }
}
