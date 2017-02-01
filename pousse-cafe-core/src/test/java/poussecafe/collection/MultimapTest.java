package poussecafe.collection;

import java.util.Set;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;

public abstract class MultimapTest<K, V> {

    private Multimap<K, V> multimap;

    private K key;

    private V added;

    private Set<V> retrieved;

    private Set<V> addedValues;

    @Test
    public void addedValueCanBeRetrieved() {
        givenMultimapWithAddedValue();
        whenRetrievingValues();
        thenAddedValueIsRetrieved();
    }

    private void givenMultimapWithAddedValue() {
        givenEmptyMultimap();
        givenKey();
        added = value();
        multimap.put(key, added);
    }

    protected void givenEmptyMultimap() {
        multimap = new Multimap<>();
    }

    protected void givenKey() {
        key = key();
    }

    protected abstract K key();

    protected abstract V value();

    private void whenRetrievingValues() {
        retrieved = multimap.get(key);
    }

    private void thenAddedValueIsRetrieved() {
        assertThat(retrieved, hasItem(added));
    }

    @Test
    public void addedValuesAreAllRetrieved() {
        givenMultimapWithAddedValues();
        whenRetrievingValues();
        thenAddedValuesAreRetrieved();
    }

    private void givenMultimapWithAddedValues() {
        givenEmptyMultimap();
        givenKey();
        addedValues = values();
        multimap.putAll(key, addedValues);
    }

    protected abstract Set<V> values();

    private void thenAddedValuesAreRetrieved() {
        assertThat(retrieved, equalTo(addedValues));
    }
}
