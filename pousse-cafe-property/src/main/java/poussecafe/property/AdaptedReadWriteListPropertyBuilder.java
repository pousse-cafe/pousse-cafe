package poussecafe.property;

import java.util.List;
import java.util.function.Function;

/**
 * @param <U> Stored list element type
 * @param <T> Property list element type
 */
public class AdaptedReadWriteListPropertyBuilder<U, T> {

    AdaptedReadWriteListPropertyBuilder(Function<U, T> readAdapter, Function<T, U> writeAdapter, List<U> list) {
        this.readAdapter = readAdapter;
        this.writeAdapter = writeAdapter;
        this.list = list;
    }

    private Function<U, T> readAdapter;

    private Function<T, U> writeAdapter;

    private List<U> list;

    public ListProperty<T> build() {
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
