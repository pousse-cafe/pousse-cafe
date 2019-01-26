package poussecafe.property;

import java.util.Objects;

public class ProtectedPropertyBuilder {

    private ProtectedPropertyBuilder() {

    }

    public static <T> ProtectedPropertyWithNoContainer<T> protect(Property<T> property) {
        Objects.requireNonNull(property);
        return new ProtectedPropertyWithNoContainer<>(property);
    }
}
