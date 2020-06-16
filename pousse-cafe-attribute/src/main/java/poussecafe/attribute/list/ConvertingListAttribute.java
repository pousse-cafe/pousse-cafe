package poussecafe.attribute.list;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
import poussecafe.attribute.ListAttribute;

import static java.util.stream.Collectors.toList;

public abstract class ConvertingListAttribute<F, T> implements ListAttribute<T> {

    public ConvertingListAttribute(List<F> list) {
        listAttribute = new BaseListAttribute<>(list);
    }

    private BaseListAttribute<F> listAttribute;

    @Override
    public List<T> value() {
        return Collections.unmodifiableList(listAttribute.stream().map(this::convertFrom).collect(toList()));
    }

    protected abstract T convertFrom(F from);

    @Override
    public void value(List<T> value) {
        Objects.requireNonNull(value);
        listAttribute.value(value.stream().map(this::convertTo).collect(toList()));
    }

    protected abstract F convertTo(T from);

    @Override
    public void add(T item) {
        listAttribute.add(convertTo(item));
    }

    @Override
    public void filter(Predicate<T> predicate) {
        value(stream().filter(predicate).collect(toList()));
    }

    @Override
    public Stream<T> stream() {
        return listAttribute.stream().map(this::convertFrom);
    }

    @Override
    public Iterator<T> iterator() {
        return value().iterator();
    }

    @Override
    public void addAll(Collection<T> values) {
        listAttribute.addAll(values.stream().map(this::convertTo).collect(toList()));
    }

    @Override
    public void clear() {
        listAttribute.clear();
    }

    @Override
    public T get(int index) {
        return convertFrom(listAttribute.get(index));
    }

    @Override
    public void set(int index,
            T item) {
        listAttribute.set(index, convertTo(item));
    }

    @Override
    public int size() {
        return listAttribute.size();
    }
}
