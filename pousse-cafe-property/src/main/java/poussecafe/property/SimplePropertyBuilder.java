package poussecafe.property;

import java.util.Objects;
import java.util.function.Supplier;

public class SimplePropertyBuilder<T> {

    SimplePropertyBuilder(Class<T> valueClass) {
        this.valueClass = valueClass;
    }

    private Class<T> valueClass;

    public ReadOnlyPropertyBuilder<T> get(Supplier<T> getter) {
        return new ReadOnlyPropertyBuilder<>(getter);
    }

    public <U> AdaptingPropertyBuilder<U, T> from(Class<U> storedType) {
        return new AdaptingPropertyBuilder<>();
    }

    public <U> AdaptingPropertyWithAdapterBuilder<U, T> fromAutoAdapting(Class<U> dataAdapterClass) {
        Objects.requireNonNull(dataAdapterClass);
        AutoAdaptingDataAdapter<U, T> dataAdapter = new AutoAdaptingDataAdapter<>(valueClass, dataAdapterClass);
        return new AdaptingPropertyWithAdapterBuilder<>(dataAdapter);
    }
}
