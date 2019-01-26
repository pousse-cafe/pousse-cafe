package poussecafe.property;

import java.util.Objects;

public class ProtectedPropertyWithNoContainer<T> {

    ProtectedPropertyWithNoContainer(Property<T> property) {
        this.property = property;
    }

    private Property<T> property;

    public ConfigurableProtectedProperty<T> of(Object container) {
        Objects.requireNonNull(container);
        return new ConfigurableProtectedProperty<>(container, property);
    }
}
