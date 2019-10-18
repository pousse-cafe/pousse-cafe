package poussecafe.attribute;

import java.util.Objects;
import java.util.function.Supplier;
import poussecafe.attribute.adapters.DataAdapter;

public class SingleAttributeBuilder<T> {

    SingleAttributeBuilder(Class<T> valueClass) {
        this.valueClass = valueClass;
    }

    private Class<T> valueClass;

    public ReadOnlyAttributeBuilder<T> read(Supplier<T> getter) {
        return new ReadOnlyAttributeBuilder<>(getter);
    }

    public <U> AdaptingAttributeBuilder<U, T> storedAs(Class<U> storedType) {
        return new AdaptingAttributeBuilder<>();
    }

    public <U> SingleAdaptingAttributeBuilder<U, T> usingDataAdapter(DataAdapter<U, T> dataAdapter) {
        return new SingleAdaptingAttributeBuilder<>(dataAdapter);
    }

    public <U> AdaptingAttributeWithAdapterBuilder<U, T> usingAutoAdapter(Class<U> autoAdapterClass) {
        Objects.requireNonNull(autoAdapterClass);
        AutoAdaptingDataAdapter<U, T> autoAdapter = new AutoAdaptingDataAdapter<>(valueClass, autoAdapterClass);
        return new AdaptingAttributeWithAdapterBuilder<>(autoAdapter);
    }
}
