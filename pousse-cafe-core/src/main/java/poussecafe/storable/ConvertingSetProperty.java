package poussecafe.storable;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public abstract class ConvertingSetProperty<F, T> implements SetProperty<T> {

    public ConvertingSetProperty(Set<F> list) {
        listProperty = new BaseSetProperty<>(list);
    }

    private BaseSetProperty<F> listProperty;

    @Override
    public Set<T> get() {
        return listProperty.stream().map(this::convertFrom).collect(toSet());
    }

    protected abstract T convertFrom(F from);

    @Override
    public void set(Set<T> value) {
        listProperty.set(value.stream().map(this::convertTo).collect(toSet()));
    }

    protected abstract F convertTo(T from);

    @Override
    public void add(T item) {
        listProperty.add(convertTo(item));
    }

    @Override
    public Stream<T> stream() {
        return listProperty.stream().map(this::convertFrom);
    }
}
