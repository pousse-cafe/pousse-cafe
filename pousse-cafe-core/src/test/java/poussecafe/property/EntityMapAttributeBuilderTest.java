package poussecafe.property;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import poussecafe.attribute.entity.EntityAttributeBuilder;
import poussecafe.attribute.entity.EntityMapAttribute;
import poussecafe.domain.Entity;
import poussecafe.domain.SimpleEntityData;
import poussecafe.entity.SimpleEntity;
import poussecafe.util.ReflectionUtils;
import poussecafe.util.StringId;

import static java.util.stream.Collectors.toMap;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class EntityMapAttributeBuilderTest {

    private EntityMapAttribute<StringId, SimpleEntity> propertyWithConversion;

    @SuppressWarnings("rawtypes")
    private Entity<?, ?> primitive() {
        return new Entity() {
            @Override
            public Entity newEntity(Class entityClass) {
                return new SimpleEntity();
            }
        };
    }

    private Map<StringId, SimpleEntity> valueWithConversion;

    private void thenValueWithConvertionIs(Map<StringId, SimpleEntity> value) {
        assertThat(valueWithConversion, is(value));
    }

    @Test
    public void readWriteWithConversion() {
        givenReadWriteAttributeWithConversion();
        whenWritingValueWithConversion();
        thenValueWithConvertionIs(valueWithConversion);
    }

    private void givenReadWriteAttributeWithConversion() {
        propertyWithConversion = EntityAttributeBuilder.entityMap(StringId.class, SimpleEntity.class)
                .entriesStoredAs(String.class, SimpleEntityData.class)
                .adaptKeyOnRead(StringId::new)
                .adaptKeyOnWrite(StringId::stringValue)
                .withMap(value)
                .build();
    }

    private Map<String, SimpleEntityData> value = initValue();

    private Map<String, SimpleEntityData> initValue() {
        SimpleEntityData data = new SimpleEntityData();
        String idString = "id";
        data.identifier().value(new StringId(idString));

        Map<String, SimpleEntityData> value = new HashMap<>();
        value.put(idString, data);
        return value;
    }

    private void whenWritingValueWithConversion() {
        propertyWithConversion.inContextOf(primitive()).value(newValue
                .entrySet()
                .stream()
                .collect(toMap(entry -> new StringId(entry.getKey()), entry -> entity(entry.getValue()))));
        valueWithConversion = propertyWithConversion.inContextOf(primitive()).value();
    }

    private Map<String, SimpleEntityData> newValue = initNewValue();

    private Map<String, SimpleEntityData> initNewValue() {
        SimpleEntityData data = new SimpleEntityData();
        String idString = "id2";
        data.identifier().value(new StringId(idString));

        Map<String, SimpleEntityData> value = new HashMap<>();
        value.put(idString, data);
        return value;
    }

    private SimpleEntity entity(SimpleEntityData data) {
        SimpleEntity entity = new SimpleEntity();
        ReflectionUtils.access(entity).set("attributes", data);
        return entity;
    }
}
