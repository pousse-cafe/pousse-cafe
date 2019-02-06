package poussecafe.attribute;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * @param <U> Stored list element type
 * @param <T> Attribute list element type
 */
public class AdaptingReadWriteListAttributeBuilder<U, T> {

    AdaptingReadWriteListAttributeBuilder(Function<U, T> readAdapter, Function<T, U> writeAdapter) {
        this.readAdapter = readAdapter;
        this.writeAdapter = writeAdapter;
    }

    private Function<U, T> readAdapter;

    private Function<T, U> writeAdapter;

    public AdaptedReadWriteListAttributeBuilder<U, T> withList(List<U> list) {
        Objects.requireNonNull(list);
        return new AdaptedReadWriteListAttributeBuilder<>(readAdapter, writeAdapter, list);
    }
}
