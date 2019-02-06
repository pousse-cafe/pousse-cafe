package poussecafe.property;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import poussecafe.attribute.MapAttribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.util.StringKey;

import static java.util.stream.Collectors.toMap;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MapAttributeBuilderTest {

    @Test
    public void readWriteNoConversion() {
        givenReadWriteAttributeWithoutConversion();
        whenWritingValueWithoutConversion();
        thenValueWithoutConvertionIs(newValue);
    }

    private void givenReadWriteAttributeWithoutConversion() {
        propertyWithoutConversion = AttributeBuilder.map(String.class, String.class)
                .withMap(value)
                .build();
    }

    private MapAttribute<String, String> propertyWithoutConversion;

    private Map<String, String> value = initValue();

    private Map<String, String> initValue() {
        Map<String, String> value = new HashMap<>();
        value.put("key", "100");
        return value;
    }

    private void whenWritingValueWithoutConversion() {
        propertyWithoutConversion.value(newValue);
        valueWithoutConversion = propertyWithoutConversion.value();
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

    private Map<StringKey, BigDecimal> valueWithConversion;

    private void thenValueWithConvertionIs(Map<StringKey, BigDecimal> value) {
        assertThat(valueWithConversion, is(value));
    }

    @Test
    public void readWriteWithConversion() {
        givenReadWriteAttributeWithConversion();
        whenWritingValueWithConversion();
        thenValueWithConvertionIs(valueWithConversion);
    }

    private void givenReadWriteAttributeWithConversion() {
        propertyWithConversion = AttributeBuilder.map(StringKey.class, BigDecimal.class)
                .from(String.class, String.class)
                .adaptOnGet(StringKey::new, BigDecimal::new)
                .adaptOnSet(StringKey::getValue, BigDecimal::toString)
                .withMap(value)
                .build();
    }

    private MapAttribute<StringKey, BigDecimal> propertyWithConversion;

    private void whenWritingValueWithConversion() {
        propertyWithConversion.value(newValue
                .entrySet()
                .stream()
                .collect(toMap(entry -> new StringKey(entry.getKey()), entry -> new BigDecimal(entry.getValue()))));
        valueWithConversion = propertyWithConversion.value();
    }
}
