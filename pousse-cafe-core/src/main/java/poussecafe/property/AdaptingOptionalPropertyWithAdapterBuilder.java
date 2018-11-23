package poussecafe.property;

import java.util.function.Supplier;
import poussecafe.property.adapters.DataAdapter;

public class AdaptingOptionalPropertyWithAdapterBuilder<U, T> {

    AdaptingOptionalPropertyWithAdapterBuilder() {

    }

    DataAdapter<U, T> adapter;

    public ReadOnlyOptionalPropertyWithAdapterBuilder<U, T> get(Supplier<U> getter) {
        ReadOnlyOptionalPropertyWithAdapterBuilder<U, T> builder = new ReadOnlyOptionalPropertyWithAdapterBuilder<>();
        builder.adapter = adapter;
        builder.getter = getter;
        return builder;
    }

}
