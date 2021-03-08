package poussecafe.attribute;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import poussecafe.attribute.adapters.DataAdapters;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class SetAttributeMutableValueTest {

    @Test
    public void editUpdatesSet() {
        givenInitialSetStorage();
        givenSetAttribute();
        whenEditing();
        thenValueIs(valueAfterEdition());
        thenSetStorageIs(storageAfterEdition());
        thenGetExpected(valueAfterEdition());
        thenContainsExpectedItems(valueAfterEdition());
    }

    private void givenInitialSetStorage() {
        storage = new HashSet<>();
        storage.add("1");
        storage.add("2");
        storage.add("3");
        storage.add("4");
    }

    private Set<String> storage;

    private void givenSetAttribute() {
        attribute = AttributeBuilder.set(BigDecimal.class)
                .usingItemDataAdapter(DataAdapters.stringBigDecimal())
                .withSet(storage)
                .build();
    }

    private SetAttribute<BigDecimal> attribute;

    private void whenEditing() {
        attribute.value().edit()
            .remove(new BigDecimal("1"))
            .add(new BigDecimal("5"))
            .finish();
    }

    private void thenValueIs(Set<BigDecimal> expected) {
        assertThat(attribute.value(), equalTo(expected));
    }

    private Set<BigDecimal> valueAfterEdition() {
        var map = new HashSet<BigDecimal>();
        map.add(new BigDecimal("2"));
        map.add(new BigDecimal("3"));
        map.add(new BigDecimal("4"));
        map.add(new BigDecimal("5"));
        return map;
    }

    private void thenSetStorageIs(Set<String> expected) {
        assertThat(storage.size(), is(expected.size()));
        assertTrue(storage.containsAll(expected));
    }

    private Set<String> storageAfterEdition() {
        var list = new HashSet<String>();
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        return list;
    }

    private void thenGetExpected(Set<BigDecimal> expected) {
        for(BigDecimal element : expected) {
            assertTrue(attribute.value().contains(element));
        }
    }

    private void thenContainsExpectedItems(Set<BigDecimal> expected) {
        assertTrue(attribute.value().equals(expected));
    }

    @Test
    public void editUpdatesCollection() {
        givenInitialCollectionStorage();
        givenSetAttributeWithCollection();
        whenEditing();
        thenValueIs(valueAfterEdition());
        thenGetExpected(valueAfterEdition());
        thenContainsExpectedItems(valueAfterEdition());
    }

    private void givenInitialCollectionStorage() {
        listStorage = new ArrayList<>();
        listStorage.add("1");
        listStorage.add("2");
        listStorage.add("3");
        listStorage.add("4");
    }

    private void givenSetAttributeWithCollection() {
        attribute = AttributeBuilder.set(BigDecimal.class)
                .usingItemDataAdapter(DataAdapters.stringBigDecimal())
                .withCollection(listStorage)
                .build();
    }

    private List<String> listStorage;
}
