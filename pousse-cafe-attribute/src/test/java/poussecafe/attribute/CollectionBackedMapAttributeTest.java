package poussecafe.attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.Test;
import poussecafe.attribute.adapters.DataAdapters;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static poussecafe.collection.Collections.edit;

public class CollectionBackedMapAttributeTest {

    @Test
    public void reset() {
        givenCollection();
        givenCollectedBackedMapAttribute();
        whenResetting();
        thenCollectionEquals(allItems());
    }

    private void givenCollection() {
        allItems().stream().forEach(collection::add);
    }

    private List<MapBackingCollectionItem> allItems() {
        var expectedItems = new ArrayList<MapBackingCollectionItem>();
        expectedItems.add(item("key1", "value1"));
        expectedItems.add(item("key2", "value2"));
        expectedItems.add(item("key3", "value3"));
        return expectedItems;
    }

    private MapBackingCollectionItem item(String key, String value) {
        return new MapBackingCollectionItem(key, value);
    }

    private List<MapBackingCollectionItem> collection = new ArrayList<>();

    private void givenCollectedBackedMapAttribute() {
        attribute = AttributeBuilder.map(String.class, MapBackingCollectionItem.class)
                .usingItemDataAdapter(DataAdapters.identity())
                .withKeyExtractor(item -> item.key)
                .withCollection(collection)
                .build();
    }

    private MapAttribute<String, MapBackingCollectionItem> attribute;

    private void whenResetting() {
        attribute.value(allItemsMap());
    }

    private Map<String, MapBackingCollectionItem> allItemsMap() {
        var map = new HashMap<String, MapBackingCollectionItem>();
        allItems().stream().forEach(item -> map.put(item.key, item));
        return map;
    }

    private void thenCollectionEquals(List<MapBackingCollectionItem> allItems) {
        assertThat(collection.size(), is(allItems.size()));
        for(MapBackingCollectionItem item : allItems) {
            assertTrue(collection.stream().anyMatch(collectionItem -> collectionItem.key.equals(item.key) && collectionItem.value.equals(item.value)));
        }
    }

    @Test
    public void get() {
        givenCollection();
        givenCollectedBackedMapAttribute();
        whenGetting("key1");
        thenValueIs("value1");
    }

    private void whenGetting(String key) {
        value = attribute.get(key);
    }

    private Optional<MapBackingCollectionItem> value;

    private void thenValueIs(String expectedValue) {
        assertTrue(value.isPresent());
        assertThat(value.get().value, equalTo(expectedValue));
    }

    @Test
    public void add() {
        givenCollection();
        givenCollectedBackedMapAttribute();
        MapBackingCollectionItem newItem = item("key4", "value4");
        whenPutting(newItem);
        thenCollectionEquals(edit(allItems()).add(newItem).finish());
    }

    private void whenPutting(MapBackingCollectionItem newItem) {
        attribute.value().put(newItem.key, newItem);
    }

    @Test
    public void values() {
        givenCollection();
        givenCollectedBackedMapAttribute();
        whenGettingValue();
        thenValueIs(allItemsMap());
    }

    private void whenGettingValue() {
        map = attribute.value();
    }

    private Map<String, MapBackingCollectionItem> map;

    private void thenValueIs(Map<String, MapBackingCollectionItem> expectedValue) {
        assertThat(map, equalTo(expectedValue));
    }

    @Test
    public void remove() {
        givenCollection();
        givenCollectedBackedMapAttribute();
        whenRemoving("key1");
        thenCollectionEquals(edit(allItems()).remove(item("key1", "value1")).finish());
    }

    private void whenRemoving(String key) {
        attribute.value().remove(key);
    }

    @Test
    public void update() {
        givenCollection();
        givenCollectedBackedMapAttribute();
        MapBackingCollectionItem newItem = item("key1", "value4");
        whenPutting(newItem);
        thenCollectionEquals(edit(allItems()).remove(item("key1", "value1")).add(item("key1", "value4")).finish());
    }
}
