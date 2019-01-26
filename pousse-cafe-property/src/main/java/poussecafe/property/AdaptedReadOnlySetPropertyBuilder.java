package poussecafe.property;

import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

/**
 * @param <U> Stored list element type
 * @param <T> Property list element type
 */
public class AdaptedReadOnlySetPropertyBuilder<U, T> {

    AdaptedReadOnlySetPropertyBuilder(Function<U, T> adapter) {
        this.adapter = adapter;
    }

    private Function<U, T> adapter;

    public SetProperty<T> build(Set<U> list) {
        return new ConvertingSetProperty<U, T>(list) {
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

    public AdaptingReadWriteSetPropertyBuilder<U, T> adapt(Function<T, U> adapter) {
        Objects.requireNonNull(adapter);
        return new AdaptingReadWriteSetPropertyBuilder<>(this.adapter, adapter);
    }
}
