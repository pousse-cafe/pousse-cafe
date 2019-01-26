package poussecafe.property;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ReadOnlyOptionalPropertyBuilder<T> {

    ReadOnlyOptionalPropertyBuilder(Supplier<T> getter) {
        compositeProperty = new CompositeProperty<>();
        compositeProperty.getter = getter;
    }

    private CompositeProperty<T, ?> compositeProperty;

    public OptionalProperty<T> build() {
        return new OptionalProperty<T>() {
            @Override
            public T getNullable() {
                return compositeProperty.getter.get();
            }

            @Override
            public void setNullable(T nullableValue) {
                throw new UnsupportedOperationException();
            }
        };
    }

    public ReadWriteOptionalPropertyBuilder<T> set(Consumer<T> setter) {
        CompositeProperty<T, T> newCompositeProperty = new CompositeProperty<>();
        newCompositeProperty.getter = compositeProperty.getter;
        newCompositeProperty.setter = setter;
        return new ReadWriteOptionalPropertyBuilder<>(newCompositeProperty);
    }
}
