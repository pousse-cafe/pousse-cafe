package poussecafe.attribute;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public abstract class ConvertingSetAttribute<F, T> implements SetAttribute<T> {

    public ConvertingSetAttribute(Set<F> list) {
        setAttribute = new BaseSetAttribute<>(list);
    }

    private BaseSetAttribute<F> setAttribute;

    @Override
    public Set<T> value() {
        return setAttribute.stream().map(this::convertFrom).collect(toSet());
    }

    protected abstract T convertFrom(F from);

    @Override
    public void value(Set<T> value) {
        Objects.requireNonNull(value);
        setAttribute.value(value.stream().map(this::convertTo).collect(toSet()));
    }

    protected abstract F convertTo(T from);

    @Override
    public boolean add(T item) {
        return setAttribute.add(convertTo(item));
    }

    @Override
    public Stream<T> stream() {
        return setAttribute.stream().map(this::convertFrom);
    }

    @Override
    public boolean contains(T item) {
        return setAttribute.contains(convertTo(item));
    }

    @Override
    public int size() {
        return setAttribute.size();
    }

    @Override
    public boolean remove(T item) {
        return setAttribute.remove(convertTo(item));
    }
}
