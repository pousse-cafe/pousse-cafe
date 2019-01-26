package poussecafe.property;

import java.util.function.Function;

/**
 * @param <U> Stored list element type
 * @param <T> Property list element type
 */
public class AdaptedSetPropertyBuilder<U, T> {

    AdaptedSetPropertyBuilder(Function<U, T> adapter) {
        this.adapter = adapter;
    }

    private Function<U, T> adapter;

    public AdaptedReadOnlySetPropertyBuilder<U, T> read() {
        return new AdaptedReadOnlySetPropertyBuilder<>(adapter);
    }
}
