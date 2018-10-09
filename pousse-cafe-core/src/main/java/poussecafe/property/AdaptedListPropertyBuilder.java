package poussecafe.property;

import java.util.function.Function;

/**
 * @param <U> Stored list element type
 * @param <T> Property list element type
 */
public class AdaptedListPropertyBuilder<U, T> {

    AdaptedListPropertyBuilder(Function<U, T> adapter) {
        this.adapter = adapter;
    }

    private Function<U, T> adapter;

    public AdaptedReadOnlyListPropertyBuilder<U, T> read() {
        return new AdaptedReadOnlyListPropertyBuilder<>(adapter);
    }
}
