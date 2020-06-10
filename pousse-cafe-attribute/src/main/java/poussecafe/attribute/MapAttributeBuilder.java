package poussecafe.attribute;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import poussecafe.attribute.adapters.DataAdapter;

public class MapAttributeBuilder<K, V> {

    MapAttributeBuilder() {

    }

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

        Complete<K, V> withMap(Map<J, U> storageMap);
    }

    public <J, U> ExpectingMap<J, U, K, V> usingEntryDataAdapters(
            DataAdapter<J, K> keyAdapter,
            DataAdapter<U, V> valueAdapter) {
        Objects.requireNonNull(keyAdapter);
        Objects.requireNonNull(valueAdapter);
        DataAdapterBasedMapAttributeBuilder<J, U, K, V> builder = new DataAdapterBasedMapAttributeBuilder<>();
        builder.keyAdapter = keyAdapter;
        builder.valueAdapter = valueAdapter;
        return builder;
    }
}
