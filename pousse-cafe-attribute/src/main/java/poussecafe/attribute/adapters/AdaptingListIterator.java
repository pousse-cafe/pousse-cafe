package poussecafe.attribute.adapters;

import java.util.ListIterator;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public class AdaptingListIterator<U, T> extends AdaptingIterator<U, T> implements ListIterator<T> {

    @Override
    public boolean hasPrevious() {
        return listIterator.hasPrevious();
    }

    private ListIterator<U> listIterator;

    @Override
    public T previous() {
        return adapter().apply(listIterator.previous());
    }

    @Override
    public int nextIndex() {
        return listIterator.nextIndex();
    }

    @Override
    public int previousIndex() {
        return listIterator.previousIndex();
    }

    @Override
    public void set(T e) {
        listIterator.set(adapter.adaptSet(e));
    }

    private DataAdapter<U, T> adapter;

    @Override
    public void add(T e) {
        listIterator.add(adapter.adaptSet(e));
    }

    public static class Builder<U, T> {

        public AdaptingListIterator<U, T> build() {
            requireNonNull(iterator.listIterator);
            requireNonNull(iterator.adapter);
            return iterator;
        }

        private AdaptingListIterator<U, T> iterator = new AdaptingListIterator<>();

        public Builder<U, T> iterator(ListIterator<U> iterator) {
            this.iterator.iterator(iterator);
            this.iterator.listIterator = iterator;
            return this;
        }

        public Builder<U, T> adapter(DataAdapter<U, T> adapter) {
            iterator.adapter(adapter::adaptGet);
            iterator.adapter = adapter;
            return this;
        }

        public Builder<U, T> onRemove(Consumer<U> onRemove) {
            iterator.onRemove(onRemove);
            return this;
        }
    }

    protected AdaptingListIterator() {

    }
}
