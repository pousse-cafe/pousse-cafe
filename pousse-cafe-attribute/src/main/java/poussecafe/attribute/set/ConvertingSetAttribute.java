package poussecafe.attribute.set;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import poussecafe.attribute.SetAttribute;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public abstract class ConvertingSetAttribute<F, T> implements SetAttribute<T> {

    public ConvertingSetAttribute(Set<F> list) {
        setAttribute = new BaseSetAttribute<>(list);
    }

    private BaseSetAttribute<F> setAttribute;

    @Override
    public Set<T> value() {
        return Collections.unmodifiableSet(setAttribute.stream().map(this::convertFrom).collect(toSet()));
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
        return value().contains(item);
    }

    @Override
    public int size() {
        return setAttribute.size();
    }

    @Override
    public boolean remove(T item) {
        return setAttribute.remove(convertTo(item));
    }

    @Override
    public Iterator<T> iterator() {
        return value().iterator();
    }

    @Override
    public void addAll(Collection<T> values) {
        setAttribute.addAll(values.stream().map(this::convertTo).collect(toList()));
    }

    @Override
    public void clear() {
        setAttribute.clear();
    }

    @Override
    public boolean isEmpty() {
        return setAttribute.isEmpty();
    }
}
