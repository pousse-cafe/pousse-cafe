package poussecafe.attribute;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ReadOnlyAttributeBuilder<T> {

    ReadOnlyAttributeBuilder(Supplier<T> getter) {
        compositeAttribute = new CompositeAttribute<>();
        compositeAttribute.getter = getter;
    }

    private CompositeAttribute<T, ?> compositeAttribute;

    public ReadWriteAttributeBuilder<T> set(Consumer<T> setter) {
        CompositeAttribute<T, T> newCompositeAttribute = new CompositeAttribute<>();
        newCompositeAttribute.getter = compositeAttribute.getter;
        newCompositeAttribute.setter = setter;
        return new ReadWriteAttributeBuilder<>(newCompositeAttribute);
    }
}
