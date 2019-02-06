package poussecafe.attribute;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ReadOnlyOptionalAttributeBuilder<T> {

    ReadOnlyOptionalAttributeBuilder(Supplier<T> getter) {
        compositeAttribute = new CompositeAttribute<>();
        compositeAttribute.getter = getter;
    }

    private CompositeAttribute<T, ?> compositeAttribute;

    public ReadWriteOptionalAttributeBuilder<T> set(Consumer<T> setter) {
        CompositeAttribute<T, T> newCompositeAttribute = new CompositeAttribute<>();
        newCompositeAttribute.getter = compositeAttribute.getter;
        newCompositeAttribute.setter = setter;
        return new ReadWriteOptionalAttributeBuilder<>(newCompositeAttribute);
    }
}
