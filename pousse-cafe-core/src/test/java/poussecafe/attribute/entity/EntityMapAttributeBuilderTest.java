package poussecafe.attribute.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityBuilder;
import poussecafe.entity.SimpleEntity;
import poussecafe.runtime.ActiveAggregate;
import poussecafe.testmodule.SimpleEntityData;
import poussecafe.util.StringId;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EntityMapAttributeBuilderTest {

    @Test
    public void readWriteMapBacked() {
        givenRegisteredAggregate();
        givenMapBackedAttribute();
        whenWriting(aValue());
        thenReadValueIs(aValue());
    }

    private void givenRegisteredAggregate() {
        ActiveAggregate.instance().set(mockAggregateRoot());
    }

    @SuppressWarnings("unchecked")
    private AggregateRoot<?, ?> mockAggregateRoot() {
        var entity = mock(AggregateRoot.class);
        var entityBuilder = mock(EntityBuilder.class);
        when(entity.newEntityBuilder(SimpleEntity.class)).thenReturn(entityBuilder);
        when(entityBuilder.buildWithoutId()).thenReturn(entity(new SimpleEntityData()));
        return entity;
    }

    private SimpleEntity entity(SimpleEntityData data) {
        SimpleEntity entity = new SimpleEntity();
        entity.attributes(data);
        return entity;
    }

    private void givenMapBackedAttribute() {
        attribute = EntityAttributeBuilder.entityMap(StringId.class, SimpleEntity.class)
                .entriesStoredAs(String.class, SimpleEntityData.class)
                .adaptKeyOnRead(StringId::new)
                .adaptKeyOnWrite(StringId::stringValue)
                .withMap(map)
                .build();
    }

    private EntityMapAttribute<StringId, SimpleEntity> attribute;

    private Map<String, SimpleEntityData> map = new HashMap<>();

    private void whenWriting(Map<StringId, SimpleEntity> newValue) {
        attribute.value(newValue);
    }

    private Map<StringId, SimpleEntity> aValue() {
        SimpleEntityData data = new SimpleEntityData();
        String idString = "id";
        data.identifier().value(new StringId(idString));

        Map<StringId, SimpleEntity> value = new HashMap<>();
        value.put(new StringId(idString), entity(data));
        return value;
    }

    private void thenReadValueIs(Map<StringId, SimpleEntity> expected) {
        assertThat(attribute.value(), is(expected));
    }

    @Test
    public void readWriteCollectionBacked() {
        givenRegisteredAggregate();
        givenReadWriteAttributeWithCollectionBackedConversion();
        whenWriting(aValue());
        thenReadValueIs(aValue());
    }

    private void givenReadWriteAttributeWithCollectionBackedConversion() {
        attribute = EntityAttributeBuilder.entityMap(StringId.class, SimpleEntity.class)
                .withCollection(collection)
                .build();
    }

    private List<SimpleEntityData> collection = new ArrayList<>();
}
