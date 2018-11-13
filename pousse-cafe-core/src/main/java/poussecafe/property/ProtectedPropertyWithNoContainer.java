package poussecafe.property;

import static poussecafe.check.Checks.checkThatValue;

public class ProtectedPropertyWithNoContainer<T> {

    ProtectedPropertyWithNoContainer(Property<T> property) {
        this.property = property;
    }

    private Property<T> property;

    public ConfigurableProtectedProperty<T> of(Object container) {
        checkThatValue(container).notNull();
        return new ConfigurableProtectedProperty<>(container, property);
    }
}
