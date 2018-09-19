package poussecafe.storable;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import poussecafe.util.StringKey;

import static java.util.stream.Collectors.toMap;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MapPropertyBuilderTest {

    @Test
    public void readOnlyNoConversion() {
        givenReadOnlyPropertyWithoutConversion();
        whenReadingValueWithoutConversion();
        thenValueWithoutConvertionIs(value);
    }

    private void givenReadOnlyPropertyWithoutConversion() {
        propertyWithoutConversion = PropertyBuilder.map(String.class, String.class)
                .read()
                .build(value);
    }

    private MapProperty<String, String> propertyWithoutConversion;

    private Map<String, String> value = initValue();

    private Map<String, String> initValue() {
        Map<String, String> value = new HashMap<>();
        value.put("key", "100");
        return value;
    }

    private void whenReadingValueWithoutConversion() {
        valueWithoutConversion = propertyWithoutConversion.get();
    }

    private Map<String, String> valueWithoutConversion;

    private void thenValueWithoutConvertionIs(Map<String, String> value) {
        assertThat(valueWithoutConversion, is(value));
    }

    @Test
    public void readWriteNoConversion() {
        givenReadWritePropertyWithoutConversion();
        whenWritingValueWithoutConversion();
        thenValueWithoutConvertionIs(newValue);
    }

    private void givenReadWritePropertyWithoutConversion() {
        propertyWithoutConversion = PropertyBuilder.map(String.class, String.class)
                .read()
                .write()
                .build(value);
    }

    private void whenWritingValueWithoutConversion() {
        propertyWithoutConversion.set(newValue);
        valueWithoutConversion = propertyWithoutConversion.get();
    }

    private Map<String, String> newValue = initNewValue();

    private Map<String, String> initNewValue() {
        Map<String, String> value = new HashMap<>();
        value.put("key2", "200");
        return value;
    }

    @Test
    public void readOnlyWithConversion() {
        givenReadOnlyPropertyWithConversion();
        whenReadingValueWithConversion();
        thenValueWithConvertionIs(value
                .entrySet()
                .stream()
                .collect(toMap(entry -> new StringKey(entry.getKey()), entry -> new BigDecimal(entry.getValue()))));
    }

    private void givenReadOnlyPropertyWithConversion() {
        propertyWithConversion = PropertyBuilder.map(StringKey.class, BigDecimal.class)
                .from(String.class, String.class)
                .adapt(StringKey::new, BigDecimal::new)
                .read()
                .build(value);
    }

    private MapProperty<StringKey, BigDecimal> propertyWithConversion;

    private void whenReadingValueWithConversion() {
        valueWithConversion = propertyWithConversion.get();
    }

    private Map<StringKey, BigDecimal> valueWithConversion;

    private void thenValueWithConvertionIs(Map<StringKey, BigDecimal> value) {
        assertThat(valueWithConversion, is(value));
    }

    @Test
    public void readWriteWithConversion() {
        givenReadWritePropertyWithConversion();
        whenWritingValueWithConversion();
        thenValueWithConvertionIs(valueWithConversion);
    }

    private void givenReadWritePropertyWithConversion() {
        propertyWithConversion = PropertyBuilder.map(StringKey.class, BigDecimal.class)
                .from(String.class, String.class)
                .adapt(StringKey::new, BigDecimal::new)
                .read()
                .adapt(StringKey::getValue, BigDecimal::toString)
                .write()
                .build(value);
    }

    private void whenWritingValueWithConversion() {
        propertyWithConversion.set(newValue
                .entrySet()
                .stream()
                .collect(toMap(entry -> new StringKey(entry.getKey()), entry -> new BigDecimal(entry.getValue()))));
        valueWithConversion = propertyWithConversion.get();
    }
}
