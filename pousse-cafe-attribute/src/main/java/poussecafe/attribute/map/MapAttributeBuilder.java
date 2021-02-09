package poussecafe.attribute.map;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;
import poussecafe.attribute.MapAttribute;
import poussecafe.attribute.adapters.DataAdapter;

public class MapAttributeBuilder<K, V> {

    public Complete<K, V> withMap(Map<K, V> map) {
        Objects.requireNonNull(map);
        return new NoAdaptingMapAttributeBuilder<>(map);
    }

    public static interface Complete<K, V> {

        MapAttribute<K, V> build();
    }

    public <J, U> FunctionAdapterBasedMapAttributeBuilder<J, U, K, V> entriesStoredAs(Class<J> storedKeyType, Class<U> storedValueType) {
        return new FunctionAdapterBasedMapAttributeBuilder<>();
    }

    public static interface ExpectingReadAdapters<J, U, K, V> {

        ExpectingWriteAdapters<J, U, K, V> adaptOnRead(Function<J, K> idAdapter, Function<U, V> valueAdapter);
    }

    public static interface ExpectingWriteAdapters<J, U, K, V> {

        ExpectingMap<J, U, K, V> adaptOnWrite(Function<K, J> idAdapter, Function<V, U> valueAdapter);
    }

    public static interface ExpectingMap<J, U, K, V> {

        Complete<K, V> withMap(Map<J, ? extends U> storageMap);
    }

    public <J, U> ExpectingMap<J, U, K, V> usingEntryDataAdapters(
            DataAdapter<J, K> keyAdapter,
            DataAdapter<U, V> valueAdapter) {
        var builder = new KeyValueAdaptersBasedMapAttributeBuilder<J, U, K, V>();

        Objects.requireNonNull(keyAdapter);
        builder.keyAdapter = keyAdapter;

        Objects.requireNonNull(valueAdapter);
        builder.valueAdapter = valueAdapter;

        return builder;
    }

    public <U> ExpectingCollection<U, K, V> usingEntryDataAdapter(DataAdapter<U, Entry<K, V>> entryAdapter) {
        var builder = new EntryAdapterBasedMapAttributeBuilder<U, K, V>();

        Objects.requireNonNull(entryAdapter);
        builder.entryAdapter = entryAdapter;

        return builder;
    }

    public static interface ExpectingCollection<U, K, V> {

        Complete<K, V> withCollection(Collection<U> collection);
    }

    public <U> ExpectingKeyExtractor<U, K, V> usingItemDataAdapter(DataAdapter<U, V> itemAdapter) {
        var builder = new ItemAdapterBasedMapAttributeBuilder<U, K, V>();

        Objects.requireNonNull(itemAdapter);
        builder.itemAdapter = itemAdapter;

        return builder;
    }

    public static interface ExpectingKeyExtractor<U, K, V> {

        ExpectingCollection<U, K, V> withKeyExtractor(Function<V, K> keyExtractor);
    }
}
