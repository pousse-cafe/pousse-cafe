package poussecafe.property;

import java.util.List;
import java.util.function.Function;

import static poussecafe.check.Checks.checkThatValue;

/**
 * @param <U> Stored list element type
 * @param <T> Property list element type
 */
public class AdaptedReadOnlyListPropertyBuilder<U, T> {

    AdaptedReadOnlyListPropertyBuilder(Function<U, T> adapter) {
        this.adapter = adapter;
    }

    private Function<U, T> adapter;

    public ListProperty<T> build(List<U> list) {
        return new ConvertingListProperty<U, T>(list) {
            @Override
            protected T convertFrom(U from) {
                return adapter.apply(from);
            }

            @Override
            protected U convertTo(T from) {
                throw new UnsupportedOperationException();
            }
        };
    }

    public AdaptingReadWriteListPropertyBuilder<U, T> adapt(Function<T, U> adapter) {
        checkThatValue(adapter).notNull();
        return new AdaptingReadWriteListPropertyBuilder<>(this.adapter, adapter);
    }
}
