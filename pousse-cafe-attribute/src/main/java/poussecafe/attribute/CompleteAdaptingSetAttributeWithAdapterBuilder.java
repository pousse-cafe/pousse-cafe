package poussecafe.attribute;

import java.util.Set;

public class CompleteAdaptingSetAttributeWithAdapterBuilder<U, T> {

    CompleteAdaptingSetAttributeWithAdapterBuilder() {

    }

    AutoAdaptingDataAdapter<U, T> adapter;

    Set<U> storageSet;

    public SetAttribute<T> build() {
        return new ConvertingSetAttribute<U, T>(storageSet) {
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
