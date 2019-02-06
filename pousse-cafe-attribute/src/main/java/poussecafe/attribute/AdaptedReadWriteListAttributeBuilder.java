package poussecafe.attribute;

import java.util.List;
import java.util.function.Function;

/**
 * @param <U> Stored list element type
 * @param <T> Attribute list element type
 */
public class AdaptedReadWriteListAttributeBuilder<U, T> {

    AdaptedReadWriteListAttributeBuilder(Function<U, T> readAdapter, Function<T, U> writeAdapter, List<U> list) {
        this.readAdapter = readAdapter;
        this.writeAdapter = writeAdapter;
        this.list = list;
    }

    private Function<U, T> readAdapter;

    private Function<T, U> writeAdapter;

    private List<U> list;

    public ListAttribute<T> build() {
        return new ConvertingListAttribute<U, T>(list) {
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
