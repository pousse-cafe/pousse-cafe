package poussecafe.test;

import java.util.Optional;
import poussecafe.property.OptionalProperty;
import poussecafe.property.Property;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockProperty {

    private MockProperty() {

    }

    public static <T> Property<T> mockProperty(T value) {
        @SuppressWarnings("unchecked")
        Property<T> property = mock(Property.class);
        when(property.get()).thenReturn(value);
        return property;
    }

    public static <T> OptionalProperty<T> mockOptionalProperty(Optional<T> value) {
        @SuppressWarnings("unchecked")
        OptionalProperty<T> property = mock(OptionalProperty.class);
        when(property.get()).thenReturn(value);
        return property;
    }
}
