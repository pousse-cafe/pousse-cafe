package poussecafe.property;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * @param <U> Stored list element type
 * @param <T> Property list element type
 */
public class AdaptingReadWriteListPropertyBuilder<U, T> {

    AdaptingReadWriteListPropertyBuilder(Function<U, T> readAdapter, Function<T, U> writeAdapter) {
        this.readAdapter = readAdapter;
        this.writeAdapter = writeAdapter;
    }

    private Function<U, T> readAdapter;

    private Function<T, U> writeAdapter;

    public AdaptedReadWriteListPropertyBuilder<U, T> withList(List<U> list) {
        Objects.requireNonNull(list);
        return new AdaptedReadWriteListPropertyBuilder<>(readAdapter, writeAdapter, list);
    }
}
