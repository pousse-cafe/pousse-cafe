package poussecafe.storable;

import java.util.List;
import java.util.function.Function;
import poussecafe.storage.memory.ConvertingListProperty;

/**
 * @param <U> Stored list element type
 * @param <T> Property list element type
 */
public class AdaptedReadWriteListPropertyBuilder<U, T> {

    AdaptedReadWriteListPropertyBuilder(Function<U, T> readAdapter, Function<T, U> writeAdapter) {
        this.readAdapter = readAdapter;
        this.writeAdapter = writeAdapter;
    }

    private Function<U, T> readAdapter;

    private Function<T, U> writeAdapter;

    public ListProperty<T> build(List<U> list) {
        return new ConvertingListProperty<U, T>(list) {
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
