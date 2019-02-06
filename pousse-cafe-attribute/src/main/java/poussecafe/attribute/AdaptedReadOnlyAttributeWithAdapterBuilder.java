package poussecafe.attribute;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @param <U> Stored type
 * @param <T> Attribute type
 */
public class AdaptedReadOnlyAttributeWithAdapterBuilder<U, T> {

    AdaptedReadOnlyAttributeWithAdapterBuilder() {

    }

    AutoAdaptingDataAdapter<U, T> dataAdapter;

    Supplier<U> getter;

    public AdaptedAttributeWithAdapterBuilder<U, T> set(Consumer<U> setter) {
        Objects.requireNonNull(setter);

        AdaptedAttributeWithAdapterBuilder<U, T> builder = new AdaptedAttributeWithAdapterBuilder<>();
        builder.dataAdapter = dataAdapter;
        builder.getter = getter;
        builder.setter = setter;
        return builder;
    }
}
