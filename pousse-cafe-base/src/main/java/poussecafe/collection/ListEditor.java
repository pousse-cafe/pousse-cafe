package poussecafe.collection;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class ListEditor<T> {

    public ListEditor() {
        this(new ArrayList<>());
    }

    public ListEditor(List<T> list) {
        requireNonNull(list);
        this.list = list;
    }

    private List<T> list;

    public ListEditor<T> add(T item) {
        list.add(item);
        return this;
    }

    public ListEditor<T> remove(T item) {
        list.remove(item);
        return this;
    }

    public List<T> finish() {
        return list;
    }

    public ListEditor<T> set(int index, T element) {
        list.set(index, element);
        return this;
    }
}