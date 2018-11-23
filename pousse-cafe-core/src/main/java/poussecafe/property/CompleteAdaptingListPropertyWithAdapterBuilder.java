package poussecafe.property;

import java.util.List;

public class CompleteAdaptingListPropertyWithAdapterBuilder<U, T> {

    CompleteAdaptingListPropertyWithAdapterBuilder() {

    }

    AutoAdaptingDataAdapter<U, T> adapter;

    List<U> storageList;

    public ListProperty<T> build() {
        return new ConvertingListProperty<U, T>(storageList) {
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
