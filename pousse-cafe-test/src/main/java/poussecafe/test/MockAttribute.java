package poussecafe.test;

import java.util.Optional;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.OptionalAttribute;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockAttribute {

    private MockAttribute() {

    }

    public static <T> Attribute<T> mockAttribute(T value) {
        @SuppressWarnings("unchecked")
        Attribute<T> property = mock(Attribute.class);
        when(property.value()).thenReturn(value);
        return property;
    }

    public static <T> OptionalAttribute<T> mockOptionalAttribute(Optional<T> value) {
        @SuppressWarnings("unchecked")
        OptionalAttribute<T> property = mock(OptionalAttribute.class);
        when(property.value()).thenReturn(value);
        return property;
    }
}
