package poussecafe.attribute;

import java.util.Objects;
import java.util.function.Supplier;

public class SimpleAttributeBuilder<T> {

    SimpleAttributeBuilder(Class<T> valueClass) {
        this.valueClass = valueClass;
    }

    private Class<T> valueClass;

    public ReadOnlyAttributeBuilder<T> get(Supplier<T> getter) {
        return new ReadOnlyAttributeBuilder<>(getter);
    }

    public <U> AdaptingAttributeBuilder<U, T> from(Class<U> storedType) {
        return new AdaptingAttributeBuilder<>();
    }

    public <U> AdaptingAttributeWithAdapterBuilder<U, T> fromAutoAdapting(Class<U> dataAdapterClass) {
        Objects.requireNonNull(dataAdapterClass);
        AutoAdaptingDataAdapter<U, T> dataAdapter = new AutoAdaptingDataAdapter<>(valueClass, dataAdapterClass);
        return new AdaptingAttributeWithAdapterBuilder<>(dataAdapter);
    }
}
