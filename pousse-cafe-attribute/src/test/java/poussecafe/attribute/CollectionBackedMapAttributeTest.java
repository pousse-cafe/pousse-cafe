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

    private List<Item> allItems() {
        var expectedItems = new ArrayList<Item>();
        expectedItems.add(item("key1", "value1"));
        expectedItems.add(item("key2", "value2"));
        expectedItems.add(item("key3", "value3"));
        return expectedItems;
    }

    private static class Item {

        String key;

        String value;

        @Override
        public boolean equals(Object obj) {
            Item other = (Item) obj;
            return key.equals(other.key)
                    && value.equals(other.value);
        }
    }

    private Item item(String key, String value) {
        var item = new Item();
        item.key = key;
        item.value = value;
        return item;
    }

    private List<Item> collection = new ArrayList<>();

    private void givenCollectedBackedMapAttribute() {
        attribute = AttributeBuilder.map(String.class, Item.class)
                .usingItemDataAdapter(DataAdapters.identity())
                .withKeyExtractor(item -> item.key)
                .withCollection(collection)
                .build();
    }

    private MapAttribute<String, Item> attribute;

    private void whenResetting() {
        attribute.value(allItemsMap());
    }

    private Map<String, Item> allItemsMap() {
        var map = new HashMap<String, Item>();
        allItems().stream().forEach(item -> map.put(item.key, item));
        return map;
    }

    private void thenCollectionEquals(List<Item> allItems) {
        assertThat(collection.size(), is(allItems.size()));
        for(Item item : allItems) {
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

    private Optional<Item> value;

    private void thenValueIs(String expectedValue) {
        assertTrue(value.isPresent());
        assertThat(value.get().value, equalTo(expectedValue));
    }

    @Test
    public void add() {
        givenCollection();
        givenCollectedBackedMapAttribute();
        Item newItem = item("key4", "value4");
        whenPutting(newItem);
        thenCollectionEquals(edit(allItems()).add(newItem).finish());
    }

    private void whenPutting(Item newItem) {
        attribute.put(newItem.key, newItem);
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

    private Map<String, Item> map;

    private void thenValueIs(Map<String, Item> expectedValue) {
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
        attribute.remove(key);
    }

    @Test
    public void update() {
        givenCollection();
        givenCollectedBackedMapAttribute();
        Item newItem = item("key1", "value4");
        whenPutting(newItem);
        thenCollectionEquals(edit(allItems()).remove(item("key1", "value1")).add(item("key1", "value4")).finish());
    }
}
