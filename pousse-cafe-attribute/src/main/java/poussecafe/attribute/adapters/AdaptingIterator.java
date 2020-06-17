package poussecafe.attribute.adapters;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class AdaptingIterator<U, T> implements Iterator<T> {

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    private Iterator<U> iterator;

    @Override
    public T next() {
        U next = iterator.next();
        if(next == null) {
            return null;
        } else {
            return adapter.apply(next);
        }
    }

    private U next;

    private Function<U, T> adapter;

    @Override
    public void remove() {
        iterator.remove();
        if(onRemove != null) {
            onRemove.accept(next);
        }
    }

    private Consumer<U> onRemove;

    public static class Builder<U, T> {

        public AdaptingIterator<U, T> build() {
            requireNonNull(iterator.iterator);
            requireNonNull(iterator.adapter);
            return iterator;
        }

        private AdaptingIterator<U, T> iterator = new AdaptingIterator<>();

        public Builder<U, T> iterator(Iterator<U> iterator) {
            this.iterator.iterator = iterator;
            return this;
        }

        public Builder<U, T> adapter(Function<U, T> adapter) {
            iterator.adapter = adapter;
            return this;
        }

        public Builder<U, T> onRemove(Consumer<U> onRemove) {
            iterator.onRemove = onRemove;
            return this;
        }
    }
}
