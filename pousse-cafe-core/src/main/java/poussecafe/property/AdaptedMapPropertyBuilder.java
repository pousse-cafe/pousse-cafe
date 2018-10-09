package poussecafe.property;

import java.util.function.Function;

/**
 * @param <J> Stored key type
 * @param <U> Stored value type
 * @param <K> Property key type
 * @param <V> Property value type
 */
public class AdaptedMapPropertyBuilder<J, U, K, V> {

    AdaptedMapPropertyBuilder(Function<J, K> keyAdapter, Function<U, V> valueAdapter) {
        this.keyAdapter = keyAdapter;
        this.valueAdapter = valueAdapter;
    }

    private Function<J, K> keyAdapter;

    private Function<U, V> valueAdapter;

    public AdaptedReadOnlyMapPropertyBuilder<J, U, K, V> read() {
        return new AdaptedReadOnlyMapPropertyBuilder<>(keyAdapter, valueAdapter);
    }
}
