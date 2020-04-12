package poussecafe.test.mockito;

import java.util.Optional;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.OptionalAttribute;
import poussecafe.domain.Entity;
import poussecafe.domain.EntityAttributes;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PousseCafeMockito {

    private PousseCafeMockito() {

    }

    public static <K, D extends EntityAttributes<K>, E extends Entity<K, D>> E mockEntity(Class<E> entityClass, Class<D> entityDataClass) {
        E entityMock = mock(entityClass);
        D entityDataMock = mock(entityDataClass);
        when(entityMock.attributes()).thenReturn(entityDataMock);
        return entityMock;
    }

    public static <V> Attribute<V> mockAttribute(V value) {
        @SuppressWarnings("unchecked")
        Attribute<V> attributeMock = mock(Attribute.class);
        when(attributeMock.value()).thenReturn(value);
        return attributeMock;
    }

    public static <V> OptionalAttribute<V> mockOptionalAttribute(Optional<V> value) {
        @SuppressWarnings("unchecked")
        OptionalAttribute<V> attributeMock = mock(OptionalAttribute.class);
        when(attributeMock.value()).thenReturn(value);
        return attributeMock;
    }
}
