package poussecafe.property;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @param <U> Stored type
 * @param <T> Property type
 */
public class AdaptedReadOnlyPropertyWithAdapterBuilder<U, T> {

    AdaptedReadOnlyPropertyWithAdapterBuilder() {

    }

    AutoAdaptingDataAdapter<U, T> dataAdapter;

    Supplier<U> getter;

    public AdaptedPropertyWithAdapterBuilder<U, T> set(Consumer<U> setter) {
        Objects.requireNonNull(setter);

        AdaptedPropertyWithAdapterBuilder<U, T> builder = new AdaptedPropertyWithAdapterBuilder<>();
        builder.dataAdapter = dataAdapter;
        builder.getter = getter;
        builder.setter = setter;
        return builder;
    }
}
