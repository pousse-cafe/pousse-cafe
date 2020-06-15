package poussecafe.attribute;

import java.util.List;
import poussecafe.attribute.ListAttributeBuilder.Complete;
import poussecafe.attribute.ListAttributeBuilder.ExpectingList;
import poussecafe.attribute.adapters.DataAdapter;

import static java.util.Objects.requireNonNull;

class DataAdapterBasedListAttributeBuilder<U, T>
implements ExpectingList<U, T>, Complete<T> {

    DataAdapterBasedListAttributeBuilder() {

    }

    DataAdapter<U, T> adapter;

    @Override
    public Complete<T> withList(List<U> storageList) {
        requireNonNull(storageList);
        this.storageList = storageList;
        return this;
    }

    List<U> storageList;

    @Override
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
