package poussecafe.property;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @param <U> Stored type
 * @param <T> Property type
 */
public class AdaptedPropertyWithAdapterBuilder<U, T> {

    AdaptedPropertyWithAdapterBuilder() {

    }

    AutoAdaptingDataAdapter<U, T> dataAdapter;

    Supplier<U> getter;

    Consumer<U> setter;

    public Property<T> build() {
        return new Property<T>() {
            @Override
            public T get() {
                return dataAdapter.adaptGet(getter.get());
            }

            @Override
            public void set(T value) {
                Objects.requireNonNull(value);
                setter.accept(dataAdapter.adaptSet(value));
            }
        };
    }
}
