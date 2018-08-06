package poussecafe.storable;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public abstract class ConvertingSetProperty<F, T> implements SetProperty<T> {

    public ConvertingSetProperty(Set<F> list) {
        setProperty = new BaseSetProperty<>(list);
    }

    private BaseSetProperty<F> setProperty;

    @Override
    public Set<T> get() {
        return setProperty.stream().map(this::convertFrom).collect(toSet());
    }

    protected abstract T convertFrom(F from);

    @Override
    public void set(Set<T> value) {
        setProperty.set(value.stream().map(this::convertTo).collect(toSet()));
    }

    protected abstract F convertTo(T from);

    @Override
    public void add(T item) {
        setProperty.add(convertTo(item));
    }

    @Override
    public Stream<T> stream() {
        return setProperty.stream().map(this::convertFrom);
    }

    @Override
    public boolean contains(T item) {
        return setProperty.contains(convertTo(item));
    }

    @Override
    public int size() {
        return setProperty.size();
    }
}
