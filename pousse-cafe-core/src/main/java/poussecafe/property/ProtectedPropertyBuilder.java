package poussecafe.property;

import static poussecafe.check.Checks.checkThatValue;

public class ProtectedPropertyBuilder {

    private ProtectedPropertyBuilder() {

    }

    public static <T> ProtectedPropertyWithNoContainer<T> protect(Property<T> property) {
        checkThatValue(property).notNull();
        return new ProtectedPropertyWithNoContainer<>(property);
    }
}
