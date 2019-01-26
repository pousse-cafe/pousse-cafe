package poussecafe.property;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ReadOnlyNumberPropertyBuilder<T extends Number> {

    ReadOnlyNumberPropertyBuilder(Supplier<T> getter) {
        compositeProperty = new CompositeProperty<>();
        compositeProperty.getter = getter;
    }

    private CompositeProperty<T, ?> compositeProperty;

    public NumberProperty<T> build() {
        return new NumberProperty<T>() {
            @Override
            public T get() {
                return compositeProperty.getter.get();
            }

            @Override
            public void set(T value) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void add(T term) {
                throw new UnsupportedOperationException();
            }
        };
    }

    public OperatorLessReadWriteNumberPropertyBuilder<T> set(Consumer<T> setter) {
        CompositeProperty<T, T> newCompositeProperty = new CompositeProperty<>();
        newCompositeProperty.getter = compositeProperty.getter;
        newCompositeProperty.setter = setter;
        return new OperatorLessReadWriteNumberPropertyBuilder<>(newCompositeProperty);
    }
}
