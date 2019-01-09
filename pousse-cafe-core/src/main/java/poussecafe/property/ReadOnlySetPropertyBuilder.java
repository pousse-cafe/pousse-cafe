package poussecafe.property;

import java.util.Set;

public class ReadOnlySetPropertyBuilder<T> {

    ReadOnlySetPropertyBuilder() {

    }

    public SetProperty<T> build(Set<T> list) {
        return new BaseSetProperty<T>(list) {
            @Override
            public void set(Set<T> value) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean add(T item) {
                throw new UnsupportedOperationException();
            }
        };
    }

    public ReadWriteSetPropertyBuilder<T> write() {
        return new ReadWriteSetPropertyBuilder<>();
    }
}
