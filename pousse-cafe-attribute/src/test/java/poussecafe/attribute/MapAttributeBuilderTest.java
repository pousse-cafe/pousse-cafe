package poussecafe.attribute;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import poussecafe.util.StringId;

import static java.util.stream.Collectors.toMap;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class MapAttributeBuilderTest {

    @Test
    public void readWriteNoConversion() {
        givenReadWriteAttributeWithoutConversion();
        whenWritingValueWithoutConversion();
        thenValueWithoutConvertionIs(newValue);
        thenValueIsImmutable(valueWithoutConversion, "test", "test");
    }

    private void givenReadWriteAttributeWithoutConversion() {
        attributeWithoutConversion = AttributeBuilder.map(String.class, String.class)
                .withMap(value)
                .build();
    }

    private MapAttribute<String, String> attributeWithoutConversion;

    private Map<String, String> value = initValue();

    private Map<String, String> initValue() {
        Map<String, String> value = new HashMap<>();
        value.put("key", "100");
        return value;
    }

    private void whenWritingValueWithoutConversion() {
        attributeWithoutConversion.value(newValue);
        valueWithoutConversion = attributeWithoutConversion.value();
    }

    private Map<String, String> newValue = initNewValue();

    private Map<String, String> initNewValue() {
        Map<String, String> value = new HashMap<>();
        value.put("key2", "200");
        return value;
    }

    private Map<String, String> valueWithoutConversion;

    private void thenValueWithoutConvertionIs(Map<String, String> value) {
        assertThat(valueWithoutConversion, is(value));
    }

    private <K, V> void thenValueIsImmutable(Map<K, V> value, K newKey, V newValue) {
        boolean modificationSuccessful;
        try {
            value.put(newKey, newValue);
            modificationSuccessful = true;
        } catch (Exception e) {
            modificationSuccessful = false;
        }
        assertFalse(modificationSuccessful);
    }

    private Map<StringId, BigDecimal> valueWithConversion;

    private void thenValueWithConvertionIs(Map<StringId, BigDecimal> value) {
        assertThat(valueWithConversion, is(value));
    }

    @Test
    public void readWriteWithConversion() {
        givenReadWriteAttributeWithConversion();
        whenWritingValueWithConversion();
        thenValueWithConvertionIs(valueWithConversion);
        thenValueIsImmutable(valueWithConversion, new StringId("test"), new BigDecimal("42.00"));
    }

    private void givenReadWriteAttributeWithConversion() {
        attributeWithConversion = AttributeBuilder.map(StringId.class, BigDecimal.class)
                .entriesStoredAs(String.class, String.class)
                .adaptOnRead(StringId::new, BigDecimal::new)
                .adaptOnWrite(StringId::stringValue, BigDecimal::toString)
                .withMap(value)
                .build();
    }

    private MapAttribute<StringId, BigDecimal> attributeWithConversion;

    private void whenWritingValueWithConversion() {
        attributeWithConversion.value(newValue
                .entrySet()
                .stream()
                .collect(toMap(entry -> new StringId(entry.getKey()), entry -> new BigDecimal(entry.getValue()))));
        valueWithConversion = attributeWithConversion.value();
    }
}
