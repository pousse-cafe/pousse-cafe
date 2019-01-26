package poussecafe.property;

import org.junit.Test;
import poussecafe.property.Property;
import poussecafe.property.PropertyBuilder;
import poussecafe.util.StringKey;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SimplePropertyBuilderTest {

    @Test
    public void readWriteNoConversion() {
        givenReadWritePropertyWithoutConversion();
        whenWritingValueWithoutConversion();
        thenValueWithoutConvertionIs(newValue);
    }

    private void givenReadWritePropertyWithoutConversion() {
        propertyWithoutConversion = PropertyBuilder.simple(String.class)
                .get(() -> value)
                .set(newValue -> value = newValue)
                .build();
    }

    private Property<String> propertyWithoutConversion;

    private String value = "current";

    private void whenWritingValueWithoutConversion() {
        propertyWithoutConversion.set(newValue);
        valueWithoutConversion = propertyWithoutConversion.get();
    }

    private String newValue = "new";

    private String valueWithoutConversion;

    private void thenValueWithoutConvertionIs(String value) {
        assertThat(valueWithoutConversion, is(value));
    }

    @Test
    public void readWriteWithConversion() {
        givenReadWritePropertyWithConversion();
        whenWritingValueWithConversion();
        thenValueWithConvertionIs(new StringKey(newValue));
    }

    private void givenReadWritePropertyWithConversion() {
        propertyWithConversion = PropertyBuilder.simple(StringKey.class)
                .from(String.class)
                .adapt(StringKey::new)
                .get(() -> value)
                .adapt(StringKey::getValue)
                .set(newValue -> value = newValue)
                .build();
    }

    private Property<StringKey> propertyWithConversion;

    private void whenWritingValueWithConversion() {
        propertyWithConversion.set(new StringKey(newValue));
        valueWithConversion = propertyWithConversion.get();
    }

    private StringKey valueWithConversion;

    private void thenValueWithConvertionIs(StringKey value) {
        assertThat(valueWithConversion, is(value));
    }
}
