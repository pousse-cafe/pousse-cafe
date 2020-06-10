package poussecafe.attribute;

import java.util.Collection;
import java.util.Map.Entry;
import poussecafe.attribute.MapAttributeBuilder.Complete;
import poussecafe.attribute.MapAttributeBuilder.ExpectingCollection;
import poussecafe.attribute.adapters.DataAdapter;

class EntryAdapterBasedMapAttributeBuilder<U, K, V>
implements ExpectingCollection<U, K, V>, Complete<K, V> {

    DataAdapter<U, Entry<K, V>> entryAdapter;

    @Override
    public Complete<K, V> withCollection(Collection<U> collection) {
        this.collection = collection;
        return this;
    }

    Collection<U> collection;

    @Override
    public MapAttribute<K, V> build() {
        return new CollectionBackedMapAttribute<>(collection) {
            @Override
            protected Entry<K, V> convertFromValue(U from) {
                return entryAdapter.adaptGet(from);
            }

            @Override
            protected U convertToValue(Entry<K, V> from) {
                return entryAdapter.adaptSet(from);
            }
        };
    }
}
