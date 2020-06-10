package poussecafe.attribute;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.function.Function;
import poussecafe.attribute.MapAttributeBuilder.Complete;
import poussecafe.attribute.MapAttributeBuilder.ExpectingCollection;
import poussecafe.attribute.MapAttributeBuilder.ExpectingKeyExtractor;
import poussecafe.attribute.adapters.DataAdapter;

class ItemAdapterBasedMapAttributeBuilder<U, K, V>
implements ExpectingCollection<U, K, V>, ExpectingKeyExtractor<U, K, V>, Complete<K, V> {

    DataAdapter<U, V> itemAdapter;

    @Override
    public ExpectingCollection<U, K, V> withKeyExtractor(Function<V, K> keyExtractor) {
        this.keyExtractor = keyExtractor;
        return this;
    }

    Function<V, K> keyExtractor;

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
                V value = itemAdapter.adaptGet(from);
                return new ReadOnlyEntry<>(keyExtractor.apply(value), value);
            }

            @Override
            protected U convertToValue(Entry<K, V> from) {
                return itemAdapter.adaptSet(from.getValue());
            }
        };
    }
}
