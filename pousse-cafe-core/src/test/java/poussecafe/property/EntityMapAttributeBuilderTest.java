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
import poussecafe.util.StringKey;

import static java.util.stream.Collectors.toMap;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EntityMapAttributeBuilderTest {

    private EntityMapAttribute<StringKey, SimpleEntity> propertyWithConversion;

    @SuppressWarnings("rawtypes")
    private Entity<?, ?> primitive() {
        return new Entity() {
            @Override
            public Entity newEntity(Class entityClass) {
                return new SimpleEntity();
            }
        };
    }

    private Map<StringKey, SimpleEntity> valueWithConversion;

    private void thenValueWithConvertionIs(Map<StringKey, SimpleEntity> value) {
        assertThat(valueWithConversion, is(value));
    }

    @Test
    public void readWriteWithConversion() {
        givenReadWriteAttributeWithConversion();
        whenWritingValueWithConversion();
        thenValueWithConvertionIs(valueWithConversion);
    }

    private void givenReadWriteAttributeWithConversion() {
        propertyWithConversion = EntityAttributeBuilder.entityMap(StringKey.class, SimpleEntity.class)
                .from(String.class, SimpleEntityData.class)
                .adaptOnGet(StringKey::new)
                .adaptOnSet(StringKey::getValue)
                .withMap(value)
                .build();
    }

    private Map<String, SimpleEntityData> value = initValue();

    private Map<String, SimpleEntityData> initValue() {
        SimpleEntityData data = new SimpleEntityData();
        String keyString = "key";
        data.key().value(new StringKey(keyString));

        Map<String, SimpleEntityData> value = new HashMap<>();
        value.put(keyString, data);
        return value;
    }

    private void whenWritingValueWithConversion() {
        propertyWithConversion.inContextOf(primitive()).value(newValue
                .entrySet()
                .stream()
                .collect(toMap(entry -> new StringKey(entry.getKey()), entry -> entity(entry.getValue()))));
        valueWithConversion = propertyWithConversion.inContextOf(primitive()).value();
    }

    private Map<String, SimpleEntityData> newValue = initNewValue();

    private Map<String, SimpleEntityData> initNewValue() {
        SimpleEntityData data = new SimpleEntityData();
        String keyString = "key2";
        data.key().value(new StringKey(keyString));

        Map<String, SimpleEntityData> value = new HashMap<>();
        value.put(keyString, data);
        return value;
    }

    private SimpleEntity entity(SimpleEntityData data) {
        SimpleEntity entity = new SimpleEntity();
        ReflectionUtils.access(entity).set("attributes", data);
        return entity;
    }
}
