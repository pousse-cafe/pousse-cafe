package poussecafe.attribute.adapters;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.function.ToIntFunction;
import poussecafe.attribute.list.EditableList;
import poussecafe.collection.ListEditor;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class AdaptingList<U, T>
extends AdaptingCollection<U, T>
implements EditableList<T> {

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return mutableList.addAll(index, adaptFromTToU(c));
    }

    private Collection<? extends U> adaptFromTToU(Collection<? extends T> c) {
        return c.stream().map(item -> adapter().adaptSet(item)).collect(toList());
    }

    private List<U> mutableList;

    @Override
    public T get(int index) {
        return adapter().adaptGet(mutableList.get(index));
    }

    @Override
    public T set(int index, T element) {
        U previousValue = mutableList.set(index, adapter().adaptSet(element));
        return nullOrAdapted(previousValue);
    }

    private T nullOrAdapted(U previousValue) {
        if(previousValue == null) {
            return null;
        } else {
            return adapter().adaptGet(previousValue);
        }
    }

    @Override
    public void add(int index, T element) {
        mutableList.add(index, adapter().adaptSet(element));
    }

    @Override
    public T remove(int index) {
        U previousValue = mutableList.remove(index);
        return nullOrAdapted(previousValue);
    }

    @Override
    public int indexOf(Object o) {
        return find(o, mutableList::indexOf);
    }

    @SuppressWarnings("unchecked")
    private int find(Object o, ToIntFunction<U> finder) {
        U elementToFind;
        try {
            elementToFind = adapter().adaptSet((T) o);
        } catch (ClassCastException e) {
            return -1;
        }
        return finder.applyAsInt(elementToFind);
    }

    @Override
    public int lastIndexOf(Object o) {
        return find(o, mutableList::lastIndexOf);
    }

    @Override
    public ListIterator<T> listIterator() {
        return new AdaptingListIterator.Builder<U, T>()
                .adapter(adapter())
                .iterator(mutableList.listIterator())
                .build();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new AdaptingListIterator.Builder<U, T>()
                .adapter(adapter())
                .iterator(mutableList.listIterator(index))
                .build();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return new AdaptingList.Builder<U, T>()
                .mutableList(mutableList.subList(fromIndex, toIndex))
                .adapter(adapter())
                .build();
    }

    @Override
    public ListEditor<T> edit() {
        return new ListEditor<>(this);
    }

    public static class Builder<U, T> {

        private AdaptingList<U, T> set = new AdaptingList<>();

        public AdaptingList<U, T> build() {
            requireNonNull(set.mutableList);
            requireNonNull(set.adapter());
            return set;
        }

        public Builder<U, T> mutableList(List<U> mutableList) {
            set.mutableCollection(mutableList);
            set.mutableList = mutableList;
            return this;
        }

        public Builder<U, T> adapter(DataAdapter<U, T> adapter) {
            set.adapter(adapter);
            return this;
        }
    }

    private AdaptingList() {

    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof List) {
            return adaptedList().equals(obj);
        } else {
            return false;
        }
    }

    private List<T> adaptedList() {
        return mutableList.stream().map(element -> adapter().adaptGet(element)).collect(toList());
    }

    @Override
    public int hashCode() {
        return adaptedList().hashCode();
    }

    @Override
    public String toString() {
        return adaptedList().toString();
    }
}
