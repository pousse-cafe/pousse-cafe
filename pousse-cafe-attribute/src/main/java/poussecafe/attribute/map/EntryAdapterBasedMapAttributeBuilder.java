package poussecafe.attribute.map;

import java.util.Collection;
import java.util.Map.Entry;
import poussecafe.attribute.MapAttribute;
import poussecafe.attribute.adapters.DataAdapter;
import poussecafe.attribute.map.MapAttributeBuilder.Complete;
import poussecafe.attribute.map.MapAttributeBuilder.ExpectingCollection;

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
