package poussecafe.property;

import java.util.Set;

public class CompleteAdaptingSetPropertyWithAdapterBuilder<U, T> {

    CompleteAdaptingSetPropertyWithAdapterBuilder() {

    }

    AutoAdaptingDataAdapter<U, T> adapter;

    Set<U> storageSet;

    public SetProperty<T> build() {
        return new ConvertingSetProperty<U, T>(storageSet) {
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
