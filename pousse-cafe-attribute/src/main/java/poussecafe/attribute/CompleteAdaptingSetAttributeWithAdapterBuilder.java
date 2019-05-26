package poussecafe.attribute;

import java.util.Set;
import poussecafe.attribute.adapters.DataAdapter;

public class CompleteAdaptingSetAttributeWithAdapterBuilder<U, T> {

    CompleteAdaptingSetAttributeWithAdapterBuilder() {

    }

    DataAdapter<U, T> adapter;

    Set<U> storageSet;

    public SetAttribute<T> build() {
        return new ConvertingSetAttribute<>(storageSet) {
            @Override
            protected T convertFrom(U from) {
                return adapter.adaptGet(from);
            }

            @Override
            protected U convertTo(T from) {
                return adapter.adaptSet(from);
            }
        };
    }
}
