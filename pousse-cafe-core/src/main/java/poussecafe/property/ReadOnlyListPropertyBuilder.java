package poussecafe.property;

import java.util.List;
import java.util.function.Predicate;

public class ReadOnlyListPropertyBuilder<T> {

    ReadOnlyListPropertyBuilder() {

    }

    public ListProperty<T> build(List<T> list) {
        return new BaseListProperty<T>(list) {
            @Override
            public void set(List<T> value) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void add(T item) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void filter(Predicate<T> predicate) {
                throw new UnsupportedOperationException();
            }
        };
    }

    public ReadWriteListPropertyBuilder<T> write() {
        return new ReadWriteListPropertyBuilder<>();
    }
}
