package poussecafe.attribute;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ReadOnlyIntegerAttributeBuilder {

    ReadOnlyIntegerAttributeBuilder(Supplier<Integer> getter) {
        this.getter = getter;
    }

    private Supplier<Integer> getter;

    public ReadWriteIntegerAttributeBuilder write(Consumer<Integer> setter) {
        Objects.requireNonNull(setter);

        CompositeAttribute<Integer, Integer> compositeAttribute = new CompositeAttribute<>();
        compositeAttribute.getter = getter;
        compositeAttribute.setter = setter;

        return new ReadWriteIntegerAttributeBuilder(compositeAttribute);
    }
}
