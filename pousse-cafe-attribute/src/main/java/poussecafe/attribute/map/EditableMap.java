package poussecafe.attribute.map;

import java.util.Map;
import poussecafe.collection.MapEditor;

public interface EditableMap<K, V> extends Map<K, V> {

    MapEditor<K, V> edit();
}
