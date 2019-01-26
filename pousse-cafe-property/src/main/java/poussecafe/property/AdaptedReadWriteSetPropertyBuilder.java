package poussecafe.property;

import java.util.Set;
import java.util.function.Function;

/**
 * @param <U> Stored list element type
 * @param <T> Property list element type
 */
public class AdaptedReadWriteSetPropertyBuilder<U, T> {

    AdaptedReadWriteSetPropertyBuilder(Function<U, T> readAdapter, Function<T, U> writeAdapter, Set<U> set) {
        this.readAdapter = readAdapter;
        this.writeAdapter = writeAdapter;
        this.set = set;
    }

    private Function<U, T> readAdapter;

    private Function<T, U> writeAdapter;

    private Set<U> set;

    public SetProperty<T> build() {
        return new ConvertingSetProperty<U, T>(set) {
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
