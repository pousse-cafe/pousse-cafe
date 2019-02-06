package poussecafe.attribute;

import java.util.Set;
import java.util.function.Function;

/**
 * @param <U> Stored list element type
 * @param <T> Attribute list element type
 */
public class AdaptedReadWriteSetAttributeBuilder<U, T> {

    AdaptedReadWriteSetAttributeBuilder(Function<U, T> readAdapter, Function<T, U> writeAdapter, Set<U> set) {
        this.readAdapter = readAdapter;
        this.writeAdapter = writeAdapter;
        this.set = set;
    }

    private Function<U, T> readAdapter;

    private Function<T, U> writeAdapter;

    private Set<U> set;

    public SetAttribute<T> build() {
        return new ConvertingSetAttribute<U, T>(set) {
            @Override
            protected T convertFrom(U from) {
                return readAdapter.apply(from);
            }

            @Override
            protected U convertTo(T from) {
                return writeAdapter.apply(from);
            }
        };
    }
}
