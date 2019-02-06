package poussecafe.attribute;

import java.util.function.Supplier;
import poussecafe.attribute.adapters.DataAdapter;

public class AdaptingOptionalAttributeWithAdapterBuilder<U, T> {

    AdaptingOptionalAttributeWithAdapterBuilder() {

    }

    DataAdapter<U, T> adapter;

    public ReadOnlyOptionalAttributeWithAdapterBuilder<U, T> get(Supplier<U> getter) {
        ReadOnlyOptionalAttributeWithAdapterBuilder<U, T> builder = new ReadOnlyOptionalAttributeWithAdapterBuilder<>();
        builder.adapter = adapter;
        builder.getter = getter;
        return builder;
    }

}
