package poussecafe.property;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static poussecafe.check.Checks.checkThatValue;

public class ReadOnlyIntegerPropertyBuilder {

    ReadOnlyIntegerPropertyBuilder(Supplier<Integer> getter) {
        this.getter = getter;
    }

    private Supplier<Integer> getter;

    public ReadWriteIntegerPropertyBuilder set(Consumer<Integer> setter) {
        checkThatValue(setter).notNull();

        CompositeProperty<Integer, Integer> compositeProperty = new CompositeProperty<>();
        compositeProperty.getter = getter;
        compositeProperty.setter = setter;

        return new ReadWriteIntegerPropertyBuilder(compositeProperty);
    }
}
