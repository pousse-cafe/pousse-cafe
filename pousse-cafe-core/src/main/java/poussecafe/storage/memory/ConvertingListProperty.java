package poussecafe.storage.memory;

import java.util.List;
import java.util.function.Predicate;
import poussecafe.storable.ListProperty;

import static java.util.stream.Collectors.toList;
import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public abstract class ConvertingListProperty<F, T> implements ListProperty<T> {

    public ConvertingListProperty(List<F> list) {
        checkThat(value(list).notNull());
        this.list = list;
    }

    private List<F> list;

    @Override
    public List<T> get() {
        return list.stream().map(this::convertFrom).collect(toList());
    }

    protected abstract T convertFrom(F from);

    @Override
    public void set(List<T> value) {
        list.clear();
        list.addAll(value.stream().map(this::convertTo).collect(toList()));
    }

    protected abstract F convertTo(T from);

    @Override
    public void add(T item) {
        list.add(convertTo(item));
    }

    @Override
    public void filter(Predicate<T> predicate) {
        List<T> filteredList = get().stream().filter(predicate).collect(toList());
        set(filteredList);
    }

}
