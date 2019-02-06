package poussecafe.attribute;

import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

/**
 * @param <U> Stored list element type
 * @param <T> Attribute list element type
 */
public class AdaptingReadWriteSetAttributeBuilder<U, T> {

    AdaptingReadWriteSetAttributeBuilder(Function<U, T> readAdapter, Function<T, U> writeAdapter) {
        this.readAdapter = readAdapter;
        this.writeAdapter = writeAdapter;
    }

    private Function<U, T> readAdapter;

    private Function<T, U> writeAdapter;

    public AdaptedReadWriteSetAttributeBuilder<U, T> withSet(Set<U> set) {
        Objects.requireNonNull(set);
        return new AdaptedReadWriteSetAttributeBuilder<>(readAdapter, writeAdapter, set);
    }
}
