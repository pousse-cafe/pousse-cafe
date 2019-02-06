package poussecafe.attribute;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @param <U> Stored type
 * @param <T> Attribute type
 */
public class AdaptingReadWriteAttributeBuilder<U, T> {

    AdaptingReadWriteAttributeBuilder(Supplier<T> getter, Function<T, U> adapter) {
        this.getter = getter;
        this.adapter = adapter;
    }

    private Supplier<T> getter;

    private Function<T, U> adapter;

    public ReadWriteAttributeBuilder<T> set(Consumer<U> setter) {
        CompositeAttribute<T, T> compositeAttribute = new CompositeAttribute<>();
        compositeAttribute.getter = getter;
        compositeAttribute.setter = value -> setter.accept(adapter.apply(value));
        return new ReadWriteAttributeBuilder<>(compositeAttribute);
    }
}
