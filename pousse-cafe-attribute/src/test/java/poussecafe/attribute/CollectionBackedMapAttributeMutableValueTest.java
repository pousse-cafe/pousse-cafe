package poussecafe.attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.Test;
import poussecafe.attribute.adapters.DataAdapters;
import poussecafe.attribute.map.ReadOnlyEntry;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class CollectionBackedMapAttributeMutableValueTest {

    @Test
    public void editUpdatesMap() {
        givenInitialStorage();
        givenMapAttribute();
        whenEditing();
        thenValueIs(valueAfterEdition());
    }

    private void givenInitialStorage() {
        storage = new ArrayList<>();
        storage.add(new MapBackingCollectionItem("key1", "1"));
        storage.add(new MapBackingCollectionItem("key2", "2"));
        storage.add(new MapBackingCollectionItem("key3", "3"));
        storage.add(new MapBackingCollectionItem("key4", "4"));
    }

    private List<MapBackingCollectionItem> storage;

    private void givenMapAttribute() {
        attribute = AttributeBuilder.map(String.class, String.class)
                .usingEntryDataAdapter(DataAdapters.<MapBackingCollectionItem, Entry<String, String>> adapter(
                        item -> new ReadOnlyEntry<>(item.key, item.value),
                        entry -> new MapBackingCollectionItem(entry.getKey(), entry.getValue())))
                .withCollection(storage)
                .build();
    }

    private MapAttribute<String, String> attribute;

    private void whenEditing() {
        attribute.mutableValue().edit()
            .remove("key1")
            .put("key5", "5")
            .finish();
    }

    private void thenValueIs(Map<String, String> expected) {
        assertThat(attribute.value(), equalTo(expected));
    }

    private Map<String, String> valueAfterEdition() {
        var map = new HashMap<String, String>();
        map.put("key2", "2");
        map.put("key3", "3");
        map.put("key4", "4");
        map.put("key5", "5");
        return map;
    }
}
