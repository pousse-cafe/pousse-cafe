package poussecafe.attribute;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.junit.Test;
import poussecafe.attribute.adapters.DataAdapters;
import poussecafe.attribute.map.ImmutableEntry;
import poussecafe.util.StringId;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class CollectionBackedMapAttributeMutableValueTest {

    @Test
    public void editUpdatesMap() {
        givenInitialStorage();
        givenMapAttribute();
        whenEditing();
        thenValueIs(valueAfterEdition());
        thenStorageHasSameContentAs(storageAfterEdition());
        thenGetExpected(valueAfterEdition());
        thenContainsExpectedKeys(valueAfterEdition().keySet());
    }

    private void givenInitialStorage() {
        storage = new ArrayList<>();
        storage.add(new ImmutableEntry<>("key1", "1"));
        storage.add(new ImmutableEntry<>("key2", "2"));
        storage.add(new ImmutableEntry<>("key3", "3"));
        storage.add(new ImmutableEntry<>("key4", "4"));
    }

    private List<ImmutableEntry<String, String>> storage;

    private void givenMapAttribute() {
        attribute = AttributeBuilder.map(StringId.class, BigDecimal.class)
                .usingEntryDataAdapter(DataAdapters.<ImmutableEntry<String, String>, Entry<StringId, BigDecimal>> adapter(
                        item -> new ImmutableEntry<>(new StringId(item.getKey()), new BigDecimal(item.getValue())),
                        entry -> new ImmutableEntry<>(entry.getKey().stringValue(), entry.getValue().toString())))
                .withCollection(storage)
                .build();
    }

    private MapAttribute<StringId, BigDecimal> attribute;

    private void whenEditing() {
        attribute.value().edit()
            .remove(new StringId("key1"))
            .put(new StringId("key5"), new BigDecimal("5"))
            .put(new StringId("key3"), new BigDecimal("3.1"))
            .finish();
    }

    private void thenValueIs(Map<StringId, BigDecimal> expected) {
        assertThat(attribute.value(), equalTo(expected));
    }

    private Map<StringId, BigDecimal> valueAfterEdition() {
        var map = new HashMap<StringId, BigDecimal>();
        map.put(new StringId("key2"), new BigDecimal("2"));
        map.put(new StringId("key3"), new BigDecimal("3.1"));
        map.put(new StringId("key4"), new BigDecimal("4"));
        map.put(new StringId("key5"), new BigDecimal("5"));
        return map;
    }

    private void thenStorageHasSameContentAs(List<ImmutableEntry<String, String>> expected) {
        assertThat(storage.size(), is(expected.size()));
        assertTrue(storage.containsAll(expected));
    }

    private List<ImmutableEntry<String, String>> storageAfterEdition() {
        var map = new ArrayList<ImmutableEntry<String, String>>();
        map.add(new ImmutableEntry<>("key2", "2"));
        map.add(new ImmutableEntry<>("key3", "3.1"));
        map.add(new ImmutableEntry<>("key4", "4"));
        map.add(new ImmutableEntry<>("key5", "5"));
        return map;
    }

    private void thenGetExpected(Map<StringId, BigDecimal> expected) {
        for(Entry<StringId, BigDecimal> entry : expected.entrySet()) {
            assertThat(attribute.get(entry.getKey()).orElseThrow(), equalTo(entry.getValue()));
        }
    }

    private void thenContainsExpectedKeys(Set<StringId> expected) {
        for(StringId expectedKey : expected) {
            assertTrue(attribute.value().containsKey(expectedKey));
        }
    }
}
