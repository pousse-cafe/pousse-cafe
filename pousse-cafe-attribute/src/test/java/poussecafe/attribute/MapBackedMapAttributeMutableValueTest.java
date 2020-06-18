package poussecafe.attribute;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import poussecafe.attribute.adapters.DataAdapters;
import poussecafe.util.StringId;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class MapBackedMapAttributeMutableValueTest {

    @Test
    public void editUpdatesMap() {
        givenInitialStorage();
        givenMapAttribute();
        whenEditing();
        thenStorageIs(storageAfterEdition());
    }

    private void givenInitialStorage() {
        storage = new HashMap<>();
        storage.put("key1", "1");
        storage.put("key2", "2");
        storage.put("key3", "3");
        storage.put("key4", "4");
    }

    private Map<String, String> storage;

    private void givenMapAttribute() {
        attribute = AttributeBuilder.map(StringId.class, BigDecimal.class)
                .usingEntryDataAdapters(DataAdapters.stringId(StringId.class), DataAdapters.stringBigDecimal())
                .withMap(storage)
                .build();
    }

    private MapAttribute<StringId, BigDecimal> attribute;

    private void whenEditing() {
        attribute.value().edit()
            .remove(new StringId("key1"))
            .put(new StringId("key5"), new BigDecimal("5"))
            .finish();
    }

    private void thenStorageIs(Map<String, String> expected) {
        assertThat(storage, equalTo(expected));
    }

    private Map<String, String> storageAfterEdition() {
        var map = new HashMap<String, String>();
        map.put("key2", "2");
        map.put("key3", "3");
        map.put("key4", "4");
        map.put("key5", "5");
        return map;
    }
}
