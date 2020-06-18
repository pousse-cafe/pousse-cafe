package poussecafe.attribute.map;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.function.Function;
import poussecafe.attribute.MapAttribute;
import poussecafe.attribute.adapters.DataAdapter;
import poussecafe.attribute.map.MapAttributeBuilder.Complete;
import poussecafe.attribute.map.MapAttributeBuilder.ExpectingCollection;
import poussecafe.attribute.map.MapAttributeBuilder.ExpectingKeyExtractor;

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
                return new ImmutableEntry<>(keyExtractor.apply(value), value);
            }

            @Override
            protected U convertToValue(Entry<K, V> from) {
                return itemAdapter.adaptSet(from.getValue());
            }
        };
    }
}
