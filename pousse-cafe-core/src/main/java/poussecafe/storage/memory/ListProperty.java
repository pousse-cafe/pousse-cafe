package poussecafe.storage.memory;

import java.util.List;
import java.util.function.Predicate;
import poussecafe.storable.Property;

public interface ListProperty<T> extends Property<List<T>> {

    void add(T item);

    void filter(Predicate<T> predicate);
}
