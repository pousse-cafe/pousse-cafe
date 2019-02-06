package poussecafe.attribute;

import java.util.List;

public class CompleteAdaptingListAttributeWithAdapterBuilder<U, T> {

    CompleteAdaptingListAttributeWithAdapterBuilder() {

    }

    AutoAdaptingDataAdapter<U, T> adapter;

    List<U> storageList;

    public ListAttribute<T> build() {
        return new ConvertingListAttribute<U, T>(storageList) {
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
