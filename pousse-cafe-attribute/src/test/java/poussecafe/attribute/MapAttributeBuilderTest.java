package poussecafe.attribute;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.Test;
import poussecafe.attribute.adapters.DataAdapter;
import poussecafe.attribute.adapters.DataAdapters;
import poussecafe.util.StringId;

import static java.util.stream.Collectors.toMap;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;

public class MapAttributeBuilderTest {

    @Test
    public void readWriteNoConversion() {
        givenReadWriteAttributeWithoutConversion();
        whenWritingValueWithoutConversion();
        thenValueWithoutConvertionIs(newValue);
        thenValueIsImmutable(valueWithoutConversion, "test", "test");
    }

    private void givenReadWriteAttributeWithoutConversion() {
        attributeWithoutConversion = AttributeBuilder.map(String.class, String.class)
                .withMap(value)
                .build();
    }

    private MapAttribute<String, String> attributeWithoutConversion;

    private Map<String, String> value = initValue();

    private Map<String, String> initValue() {
        Map<String, String> value = new HashMap<>();
        value.put("key", "100");
        return value;
    }

    private void whenWritingValueWithoutConversion() {
        attributeWithoutConversion.value(newValue);
        valueWithoutConversion = attributeWithoutConversion.value();
    }

    private Map<String, String> newValue = initNewValue();

    private Map<String, String> initNewValue() {
        Map<String, String> value = new HashMap<>();
        value.put("test", "42.00");
        return value;
    }

    private Map<String, String> valueWithoutConversion;

    private void thenValueWithoutConvertionIs(Map<String, String> value) {
        assertThat(valueWithoutConversion, is(value));
    }

    private <K, V> void thenValueIsImmutable(Map<K, V> value, K newKey, V newValue) {
        boolean modificationSuccessful;
        try {
            value.put(newKey, newValue);
            modificationSuccessful = true;
        } catch (Exception e) {
            modificationSuccessful = false;
        }
        assertFalse(modificationSuccessful);
    }

    private Map<StringId, BigDecimal> valueWithConversion;

    @Test
    public void readWriteWithFunctionAdapters() {
        givenReadWriteAttributeWithFunctionAdapters();
        whenWritingValueWithConversion();
        thenValueIs(attributeWithConversion, new StringId("test"), new BigDecimal("42.00"));
        thenValueIsImmutable(valueWithConversion, new StringId("test"), new BigDecimal("42.00"));
    }

    private void givenReadWriteAttributeWithFunctionAdapters() {
        attributeWithConversion = AttributeBuilder.map(StringId.class, BigDecimal.class)
                .entriesStoredAs(String.class, String.class)
                .adaptOnRead(StringId::new, BigDecimal::new)
                .adaptOnWrite(StringId::stringValue, BigDecimal::toString)
                .withMap(value)
                .build();
    }

    private MapAttribute<StringId, BigDecimal> attributeWithConversion;

    private void whenWritingValueWithConversion() {
        attributeWithConversion.value(newValue
                .entrySet()
                .stream()
                .collect(toMap(entry -> new StringId(entry.getKey()), entry -> new BigDecimal(entry.getValue()))));
        valueWithConversion = attributeWithConversion.value();
    }

    @Test
    public void readWriteWithDataAdapters() {
        givenReadWriteAttributeWithDataAdapters();
        whenWritingValueWithConversion();
        thenValueIs(attributeWithConversion, new StringId("test"), new BigDecimal("42.00"));
        thenValueIsImmutable(valueWithConversion, new StringId("test"), new BigDecimal("42.00"));
    }

    private void givenReadWriteAttributeWithDataAdapters() {
        attributeWithConversion = AttributeBuilder.map(StringId.class, BigDecimal.class)
                .usingEntryDataAdapters(DataAdapters.stringId(StringId.class), DataAdapters.stringBigDecimal())
                .withMap(value)
                .build();
    }

    @Test
    public void readWriteCollectionWithDataAdapters() {
        givenReadWriteCollectionAttributeWithDataAdapters();
        whenWritingValueWithConversion();
        thenValueIs(attributeWithConversion, new StringId("test"), new BigDecimal("42.00"));
        thenValueIsImmutable(valueWithConversion, new StringId("test"), new BigDecimal("42.00"));
    }

    private void givenReadWriteCollectionAttributeWithDataAdapters() {
        attributeWithConversion = AttributeBuilder.map(StringId.class, BigDecimal.class)
                .usingEntryDataAdapter(itemAdapter)
                .withCollection(collection)
                .build();
    }

    private List<String> collection = new ArrayList<>();

    private DataAdapter<String, Entry<StringId, BigDecimal>> itemAdapter = new DataAdapter<>() {
        @Override
        public Entry<StringId, BigDecimal> adaptGet(String storedValue) {
            return new ReadOnlyEntry<>(new StringId(storedValue.split(" ")[0]),
                    new BigDecimal(storedValue.split(" ")[1]));
        }

        @Override
        public String adaptSet(Entry<StringId, BigDecimal> valueToStore) {
            return valueToStore.getKey().stringValue() + " " + valueToStore.getValue().toString();
        }
    };

    @Test
    public void readWriteOtherCollectionWithDataAdapters() {
        givenReadWriteOtherCollectionAttributeWithDataAdapters();
        Item value = new Item(new StringId("test"), new BigDecimal("42.00"));
        otherAttributeWithConversion.put(value.key, value);
        thenValueIs(otherAttributeWithConversion, new StringId("test"), value);
        thenValueIsImmutable(otherAttributeWithConversion.value(), value.key, new Item());
    }

    private void givenReadWriteOtherCollectionAttributeWithDataAdapters() {
        otherAttributeWithConversion = AttributeBuilder.map(StringId.class, Item.class)
                .usingItemDataAdapter(otherItemAdapter)
                .withKeyExtractor(item -> item.key)
                .withCollection(otherCollection)
                .build();
    }

    private MapAttribute<StringId, Item> otherAttributeWithConversion;

    private static class Item {

        StringId key;

        BigDecimal value;

        public Item() {

        }

        public Item(StringId key, BigDecimal value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            Item other = (Item) obj;
            return key.equals(other.key) && value.equals(other.value);
        }
    }

    private DataAdapter<ItemData, Item> otherItemAdapter = new DataAdapter<>() {
        @Override
        public Item adaptGet(ItemData storedValue) {
            Item item = new Item();
            item.key = new StringId(storedValue.key);
            item.value = new BigDecimal(storedValue.value);
            return item;
        }

        @Override
        public ItemData adaptSet(Item valueToStore) {
            ItemData data = new ItemData();
            data.key = valueToStore.key.stringValue();
            data.value = valueToStore.value.toString();
            return data;
        }
    };

    private List<ItemData> otherCollection = new ArrayList<>();

    private static class ItemData {

        String key;

        String value;
    }

    private <K, V> void thenValueIs(MapAttribute<K, V> attribute, K stringId, V item) {
        assertThat(attribute.get(stringId).orElseThrow(), equalTo(item));
    }
}
