package poussecafe.test.mockito;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.IntegerAttribute;
import poussecafe.attribute.ListAttribute;
import poussecafe.attribute.MapAttribute;
import poussecafe.attribute.NumberAttribute;
import poussecafe.attribute.OptionalAttribute;
import poussecafe.attribute.SetAttribute;
import poussecafe.attribute.entity.EntityAttribute;
import poussecafe.attribute.entity.EntityMapAttribute;
import poussecafe.attribute.entity.OptionalEntityAttribute;
import poussecafe.attribute.entity.SimpleEntityMap;
import poussecafe.attribute.list.SimpleEditableList;
import poussecafe.attribute.map.SimpleEditableMap;
import poussecafe.attribute.set.SimpleEditableSet;
import poussecafe.domain.Entity;
import poussecafe.domain.EntityAttributes;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class PousseCafeMockito {

    public static <V> Attribute<V> mockAttribute(V value) {
        Attribute<V> attributeMock = mock(Attribute.class);
        when(attributeMock.value()).thenReturn(value);
        return attributeMock;
    }

    public static <V> OptionalAttribute<V> mockOptionalAttribute(Optional<V> value) {
        OptionalAttribute<V> attributeMock = mock(OptionalAttribute.class);
        when(attributeMock.value()).thenReturn(value);
        return attributeMock;
    }

    public static <V> ListAttribute<V> mockListAttribute(List<V> value) {
        ListAttribute<V> attributeMock = mock(ListAttribute.class);
        when(attributeMock.value()).thenReturn(new SimpleEditableList<>(value));
        return attributeMock;
    }

    public static <V> SetAttribute<V> mockSetAttribute(Set<V> value) {
        SetAttribute<V> attributeMock = mock(SetAttribute.class);
        when(attributeMock.value()).thenReturn(new SimpleEditableSet<>(value));
        return attributeMock;
    }

    public static <K, V> MapAttribute<K, V> mockMapAttribute(Map<K, V> value) {
        MapAttribute<K, V> attributeMock = mock(MapAttribute.class);
        when(attributeMock.value()).thenReturn(new SimpleEditableMap<>(value));
        return attributeMock;
    }

    public static IntegerAttribute mockIntegerAttribute(Integer value) {
        IntegerAttribute attributeMock = mock(IntegerAttribute.class);
        when(attributeMock.value()).thenReturn(value);
        return attributeMock;
    }

    public static <N extends Number> NumberAttribute<N> mockNumberAttribute(N value) {
        NumberAttribute<N> attributeMock = mock(NumberAttribute.class);
        when(attributeMock.value()).thenReturn(value);
        return attributeMock;
    }

    public static <K, D extends EntityAttributes<K>, E extends Entity<K, D>> E mockEntity(Class<E> entityClass, Class<D> entityDataClass) {
        E entityMock = mock(entityClass);
        D entityDataMock = mock(entityDataClass);
        when(entityMock.attributes()).thenReturn(entityDataMock);
        return entityMock;
    }

    public static <K, D extends EntityAttributes<K>, E extends Entity<K, D>>
    EntityAttribute<E> mockEntityAttribute(E value) {
        EntityAttribute<E> attributeMock = mock(EntityAttribute.class);
        when(attributeMock.value()).thenReturn(value);
        return attributeMock;
    }

    public static <K, D extends EntityAttributes<K>, E extends Entity<K, D>>
    OptionalEntityAttribute<E> mockOptionalEntityAttribute(Optional<E> value) {
        OptionalEntityAttribute<E> attributeMock = mock(OptionalEntityAttribute.class);
        when(attributeMock.value()).thenReturn(value);
        return attributeMock;
    }

    public static <K, D extends EntityAttributes<K>, E extends Entity<K, D>>
    EntityMapAttribute<K, E> mockEntityMapAttribute(Map<K, E> map) {
        EntityMapAttribute<K, E> attributeMock = mock(EntityMapAttribute.class);
        when(attributeMock.value()).thenReturn(new SimpleEntityMap<>(new SimpleEditableMap<>(map)));
        return attributeMock;
    }

    private PousseCafeMockito() {

    }
}
