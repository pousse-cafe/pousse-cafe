package poussecafe.attribute.set;

import java.util.Set;
import poussecafe.attribute.SetAttribute;
import poussecafe.attribute.adapters.DataAdapter;
import poussecafe.attribute.set.SetAttributeBuilder.Complete;
import poussecafe.attribute.set.SetAttributeBuilder.ExpectingSet;

import static java.util.Objects.requireNonNull;

class DataAdapterBasedSetAttributeBuilder<U, T>
implements ExpectingSet<U, T>, Complete<T> {

    DataAdapterBasedSetAttributeBuilder(DataAdapter<U, T> adapter) {
        requireNonNull(adapter);
        this.adapter = adapter;
    }

    private DataAdapter<U, T> adapter;

    @Override
    public Complete<T> withSet(Set<U> storageSet) {
        requireNonNull(storageSet);
        this.storageSet = storageSet;
        return this;
    }

    private Set<U> storageSet;

    @Override
    public SetAttribute<T> build() {
        return new AdaptingSetAttribute<>(storageSet) {
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
