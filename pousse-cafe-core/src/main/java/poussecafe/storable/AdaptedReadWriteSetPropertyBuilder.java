package poussecafe.storable;

import java.util.Set;
import java.util.function.Function;

/**
 * @param <U> Stored list element type
 * @param <T> Property list element type
 */
public class AdaptedReadWriteSetPropertyBuilder<U, T> {

    AdaptedReadWriteSetPropertyBuilder(Function<U, T> readAdapter, Function<T, U> writeAdapter) {
        this.readAdapter = readAdapter;
        this.writeAdapter = writeAdapter;
    }

    private Function<U, T> readAdapter;

    private Function<T, U> writeAdapter;

    public SetProperty<T> build(Set<U> list) {
        return new ConvertingSetProperty<U, T>(list) {
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
