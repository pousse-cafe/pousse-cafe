package poussecafe.attribute;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @param <U> Stored type
 * @param <T> Attribute type
 */
public class AdaptingAttributeWithAdapterBuilder<U, T> {

    AdaptingAttributeWithAdapterBuilder(AutoAdaptingDataAdapter<U, T> dataAdapter) {
        this.dataAdapter = dataAdapter;
    }

    private AutoAdaptingDataAdapter<U, T> dataAdapter;

    public AdaptedReadOnlyAttributeWithAdapterBuilder<U, T> read(Supplier<U> getter) {
        Objects.requireNonNull(getter);

        AdaptedReadOnlyAttributeWithAdapterBuilder<U, T> builder = new AdaptedReadOnlyAttributeWithAdapterBuilder<>();
        builder.dataAdapter = dataAdapter;
        builder.getter = getter;
        return builder;
    }
}
