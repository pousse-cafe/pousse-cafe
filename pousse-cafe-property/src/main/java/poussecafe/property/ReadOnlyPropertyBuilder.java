package poussecafe.property;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ReadOnlyPropertyBuilder<T> {

    ReadOnlyPropertyBuilder(Supplier<T> getter) {
        compositeProperty = new CompositeProperty<>();
        compositeProperty.getter = getter;
    }

    private CompositeProperty<T, ?> compositeProperty;

    public ReadWritePropertyBuilder<T> set(Consumer<T> setter) {
        CompositeProperty<T, T> newCompositeProperty = new CompositeProperty<>();
        newCompositeProperty.getter = compositeProperty.getter;
        newCompositeProperty.setter = setter;
        return new ReadWritePropertyBuilder<>(newCompositeProperty);
    }
}
