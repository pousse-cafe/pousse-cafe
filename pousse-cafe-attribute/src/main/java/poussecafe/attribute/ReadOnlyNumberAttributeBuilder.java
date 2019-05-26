package poussecafe.attribute;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ReadOnlyNumberAttributeBuilder<T extends Number> {

    ReadOnlyNumberAttributeBuilder(Supplier<T> getter) {
        compositeAttribute = new CompositeAttribute<>();
        compositeAttribute.getter = getter;
    }

    private CompositeAttribute<T, ?> compositeAttribute;

    public OperatorLessReadWriteNumberAttributeBuilder<T> write(Consumer<T> setter) {
        CompositeAttribute<T, T> newCompositeAttribute = new CompositeAttribute<>();
        newCompositeAttribute.getter = compositeAttribute.getter;
        newCompositeAttribute.setter = setter;
        return new OperatorLessReadWriteNumberAttributeBuilder<>(newCompositeAttribute);
    }
}
