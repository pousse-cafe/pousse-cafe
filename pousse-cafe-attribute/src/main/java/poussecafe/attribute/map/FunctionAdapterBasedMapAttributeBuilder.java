package poussecafe.attribute.map;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import poussecafe.attribute.MapAttribute;
import poussecafe.attribute.map.MapAttributeBuilder.Complete;
import poussecafe.attribute.map.MapAttributeBuilder.ExpectingMap;
import poussecafe.attribute.map.MapAttributeBuilder.ExpectingReadAdapters;
import poussecafe.attribute.map.MapAttributeBuilder.ExpectingWriteAdapters;

/**
 * @param <J> Stored id type
 * @param <U> Stored value type
 * @param <K> Attribute id type
 * @param <V> Attribute value type
 */
public class FunctionAdapterBasedMapAttributeBuilder<J, U, K, V>
implements ExpectingReadAdapters<J, U, K, V>, ExpectingWriteAdapters<J, U, K, V>, ExpectingMap<J, U, K, V>, Complete<K, V> {

    @Override
    public ExpectingWriteAdapters<J, U, K, V> adaptOnRead(Function<J, K> idAdapter, Function<U, V> valueAdapter) {
        Objects.requireNonNull(idAdapter);
        readKeyAdapter = idAdapter;

        Objects.requireNonNull(valueAdapter);
        readValueAdapter = valueAdapter;
        return this;
    }

    private Function<J, K> readKeyAdapter;

    private Function<U, V> readValueAdapter;

    @Override
    public ExpectingMap<J, U, K, V> adaptOnWrite(Function<K, J> idAdapter, Function<V, U> valueAdapter) {
        Objects.requireNonNull(idAdapter);
        writeKeyAdapter = idAdapter;

        Objects.requireNonNull(valueAdapter);
        writeValueAdapter = valueAdapter;
        return this;
    }

    private Function<K, J> writeKeyAdapter;

    private Function<V, U> writeValueAdapter;

    @Override
    public Complete<K, V> withMap(Map<J, U> map) {
        Objects.requireNonNull(map);
        this.map = map;
        return this;
    }

    private Map<J, U> map;

    @Override
    public MapAttribute<K, V> build() {
        return new ConvertingMapAttribute<>(map) {
            @Override
            protected K convertFromKey(J from) {
                return readKeyAdapter.apply(from);
            }

            @Override
            protected V convertFromValue(U from) {
                return readValueAdapter.apply(from);
            }

            @Override
            protected J convertToKey(K from) {
                return writeKeyAdapter.apply(from);
            }

            @Override
            protected U convertToValue(V from) {
                return writeValueAdapter.apply(from);
            }
        };
    }
}
