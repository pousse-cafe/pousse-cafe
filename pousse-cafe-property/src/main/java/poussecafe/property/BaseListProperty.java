package poussecafe.property;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class BaseListProperty<T> implements ListProperty<T> {

    public BaseListProperty(List<T> list) {
        Objects.requireNonNull(list);
        this.list = list;
    }

    private List<T> list;

    @Override
    public List<T> get() {
        return new ArrayList<>(list);
    }

    @Override
    public void set(List<T> value) {
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
        set(stream().filter(predicate).collect(toList()));
    }

    @Override
    public Stream<T> stream() {
        return list.stream();
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

}
