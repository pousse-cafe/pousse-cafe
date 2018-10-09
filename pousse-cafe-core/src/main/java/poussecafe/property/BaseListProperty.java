package poussecafe.property;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class BaseListProperty<T> implements ListProperty<T> {

    public BaseListProperty(List<T> list) {
        checkThat(value(list).notNull());
        this.list = list;
    }

    private List<T> list;

    @Override
    public List<T> get() {
        return new ArrayList<>(list);
    }

    @Override
    public void set(List<T> value) {
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

}
