package poussecafe.attribute;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import poussecafe.attribute.adapters.DataAdapters;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class ListAttributeMutableValueTest {

    @Test
    public void editUpdatesList() {
        givenInitialStorage();
        givenListAttribute();
        whenEditing();
        thenValueIs(valueAfterEdition());
        thenStorageIs(storageAfterEdition());
        thenGetExpected(valueAfterEdition());
        thenContainsExpectedItems(valueAfterEdition());
    }

    private void givenInitialStorage() {
        storage = new ArrayList<>();
        storage.add("1");
        storage.add("2");
        storage.add("3");
        storage.add("4");
    }

    private List<String> storage;

    private void givenListAttribute() {
        attribute = AttributeBuilder.list(BigDecimal.class)
                .usingItemDataAdapter(DataAdapters.stringBigDecimal())
                .withList(storage)
                .build();
    }

    private ListAttribute<BigDecimal> attribute;

    private void whenEditing() {
        attribute.value().edit()
            .remove(new BigDecimal("1"))
            .add(new BigDecimal("5"))
            .set(1, new BigDecimal("3.1"))
            .finish();
    }

    private void thenValueIs(List<BigDecimal> expected) {
        assertThat(attribute.value(), equalTo(expected));
    }

    private List<BigDecimal> valueAfterEdition() {
        var map = new ArrayList<BigDecimal>();
        map.add(new BigDecimal("2"));
        map.add(new BigDecimal("3.1"));
        map.add(new BigDecimal("4"));
        map.add(new BigDecimal("5"));
        return map;
    }

    private void thenStorageIs(List<String> expected) {
        assertThat(storage.size(), is(expected.size()));
        assertTrue(storage.containsAll(expected));
    }

    private List<String> storageAfterEdition() {
        var list = new ArrayList<String>();
        list.add("2");
        list.add("3.1");
        list.add("4");
        list.add("5");
        return list;
    }

    private void thenGetExpected(List<BigDecimal> expected) {
        for(BigDecimal element : expected) {
            assertThat(attribute.value().indexOf(element), not(is(-1)));
            assertThat(attribute.value().lastIndexOf(element), not(is(-1)));
            assertTrue(attribute.value().contains(element));
        }
    }

    private void thenContainsExpectedItems(List<BigDecimal> expected) {
        assertTrue(attribute.value().equals(expected));
    }
}
