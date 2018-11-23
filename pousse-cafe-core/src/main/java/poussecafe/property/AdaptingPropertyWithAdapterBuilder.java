package poussecafe.property;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @param <U> Stored type
 * @param <T> Property type
 */
public class AdaptingPropertyWithAdapterBuilder<U, T> {

    AdaptingPropertyWithAdapterBuilder(AutoAdaptingDataAdapter<U, T> dataAdapter) {
        this.dataAdapter = dataAdapter;
    }

    private AutoAdaptingDataAdapter<U, T> dataAdapter;

    public AdaptedReadOnlyPropertyWithAdapterBuilder<U, T> get(Supplier<U> getter) {
        Objects.requireNonNull(getter);

        AdaptedReadOnlyPropertyWithAdapterBuilder<U, T> builder = new AdaptedReadOnlyPropertyWithAdapterBuilder<>();
        builder.dataAdapter = dataAdapter;
        builder.getter = getter;
        return builder;
    }
}
