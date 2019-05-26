package poussecafe.attribute;

import java.util.List;
import poussecafe.attribute.adapters.DataAdapter;

public class CompleteAdaptingListAttributeWithAdapterBuilder<U, T> {

    CompleteAdaptingListAttributeWithAdapterBuilder() {

    }

    DataAdapter<U, T> adapter;

    List<U> storageList;

    public ListAttribute<T> build() {
        return new ConvertingListAttribute<>(storageList) {
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
