package poussecafe.attribute;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @param <U> Stored type
 * @param <T> Attribute type
 */
public class AdaptedAttributeWithAdapterBuilder<U, T> {

    AdaptedAttributeWithAdapterBuilder() {

    }

    AutoAdaptingDataAdapter<U, T> dataAdapter;

    Supplier<U> getter;

    Consumer<U> setter;

    public Attribute<T> build() {
        return new Attribute<T>() {
            @Override
            public T value() {
                return dataAdapter.adaptGet(getter.get());
            }

            @Override
            public void value(T value) {
                Objects.requireNonNull(value);
                setter.accept(dataAdapter.adaptSet(value));
            }
        };
    }
}
