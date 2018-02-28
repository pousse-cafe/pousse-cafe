package poussecafe.storage.memory;

import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

public abstract class MappingListProperty<F, T> implements ListProperty<T> {

    public MappingListProperty(List<F> from) {
        this.from = from;
    }

    private List<F> from;

    @Override
    public List<T> get() {
        return from.stream().map(this::mapFrom).collect(toList());
    }

    protected abstract T mapFrom(F from);

    @Override
    public void set(List<T> value) {
        from.clear();
        from.addAll(value.stream().map(this::mapTo).collect(toList()));
    }

    protected abstract F mapTo(T from);

    @Override
    public void add(T item) {
        from.add(mapTo(item));
    }

    @Override
    public void filter(Predicate<T> predicate) {
        List<T> filteredList = get().stream().filter(predicate).collect(toList());
        set(filteredList);
    }

}
