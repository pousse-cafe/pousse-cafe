package poussecafe.storage.memory;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import poussecafe.storable.ListProperty;

import static java.util.stream.Collectors.toList;

public abstract class ConvertingListProperty<F, T> implements ListProperty<T> {

    public ConvertingListProperty(List<F> list) {
        listProperty = new BaseListProperty<>(list);
    }

    private BaseListProperty<F> listProperty;

    @Override
    public List<T> get() {
        return listProperty.stream().map(this::convertFrom).collect(toList());
    }

    protected abstract T convertFrom(F from);

    @Override
    public void set(List<T> value) {
        listProperty.set(value.stream().map(this::convertTo).collect(toList()));
    }

    protected abstract F convertTo(T from);

    @Override
    public void add(T item) {
        listProperty.add(convertTo(item));
    }

    @Override
    public void filter(Predicate<T> predicate) {
        set(stream().filter(predicate).collect(toList()));
    }

    @Override
    public Stream<T> stream() {
        return listProperty.stream().map(this::convertFrom);
    }
}
