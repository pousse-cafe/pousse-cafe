package poussecafe.attribute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class BaseListAttribute<T> implements ListAttribute<T> {

    public BaseListAttribute(List<T> list) {
        Objects.requireNonNull(list);
        this.list = list;
    }

    private List<T> list;

    @Override
    public List<T> value() {
        return new ArrayList<>(list);
    }

    @Override
    public void value(List<T> value) {
        Objects.requireNonNull(value);
        list.clear();
        list.addAll(value);
    }

    @Override
    public void add(T item) {
        list.add(item);
    }

    @Override
    public void filter(Predicate<T> predicate) {
        value(stream().filter(predicate).collect(toList()));
    }

    @Override
    public Stream<T> stream() {
        return list.stream();
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    @Override
    public void addAll(Collection<T> values) {
        list.addAll(values);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public T get(int index) {
        return list.get(index);
    }

    @Override
    public void set(int index,
            T item) {
        list.set(index, item);
    }

    @Override
    public void clear() {
        list.clear();
    }
}
