package poussecafe.attribute.adapters;

import java.util.Iterator;
import java.util.Map.Entry;

import static java.util.Objects.requireNonNull;

public class AdaptingMutableMapValuesIterator<L, U, K, V>
implements Iterator<V> {

    @Override
    public boolean hasNext() {
        return entrySetIterator.hasNext();
    }

    private Iterator<Entry<L, U>> entrySetIterator;

    @Override
    public V next() {
        var next = entrySetIterator.next();
        return valueAdapter.adaptGet(next.getValue());
    }

    @Override
    public void remove() {
        entrySetIterator.remove();
    }

    private DataAdapter<U, V> valueAdapter;

    public static class Builder<L, U, K, V> {

        private AdaptingMutableMapValuesIterator<L, U, K, V> iterator = new AdaptingMutableMapValuesIterator<>();

        public AdaptingMutableMapValuesIterator<L, U, K, V> build() {
            requireNonNull(iterator.entrySetIterator);
            requireNonNull(iterator.valueAdapter);
            return iterator;
        }

        public Builder<L, U, K, V> entrySetIterator(Iterator<Entry<L, U>> entrySetIterator) {
            iterator.entrySetIterator = entrySetIterator;
            return this;
        }

        public Builder<L, U, K, V> valueAdapter(DataAdapter<U, V> valueAdapter) {
            iterator.valueAdapter = valueAdapter;
            return this;
        }
    }

    private AdaptingMutableMapValuesIterator() {

    }
}
