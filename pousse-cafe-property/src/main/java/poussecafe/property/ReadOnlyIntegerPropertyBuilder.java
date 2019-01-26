package poussecafe.property;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ReadOnlyIntegerPropertyBuilder {

    ReadOnlyIntegerPropertyBuilder(Supplier<Integer> getter) {
        this.getter = getter;
    }

    private Supplier<Integer> getter;

    public ReadWriteIntegerPropertyBuilder set(Consumer<Integer> setter) {
        Objects.requireNonNull(setter);

        CompositeProperty<Integer, Integer> compositeProperty = new CompositeProperty<>();
        compositeProperty.getter = getter;
        compositeProperty.setter = setter;

        return new ReadWriteIntegerPropertyBuilder(compositeProperty);
    }
}
