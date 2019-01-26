package poussecafe.property;

import java.util.Objects;

public class ReadWritePropertyBuilder<T> {

    ReadWritePropertyBuilder(CompositeProperty<T, T> compositeProperty) {
        this.compositeProperty = compositeProperty;
    }

    private CompositeProperty<T, T> compositeProperty;

    public Property<T> build() {
        return new Property<T>() {
            @Override
            public T get() {
                return compositeProperty.getter.get();
            }

            @Override
            public void set(T value) {
                Objects.requireNonNull(value);
                compositeProperty.setter.accept(value);
            }
        };
    }
}
