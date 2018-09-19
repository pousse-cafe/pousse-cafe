package poussecafe.storable;

import org.junit.Test;
import poussecafe.util.StringKey;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SimplePropertyBuilderTest {

    @Test
    public void readOnlyNoConversion() {
        givenReadOnlyPropertyWithoutConversion();
        whenReadingValueWithoutConversion();
        thenValueWithoutConvertionIs(value);
    }

    private void givenReadOnlyPropertyWithoutConversion() {
        propertyWithoutConversion = PropertyBuilder.simple(String.class)
                .get(() -> value)
                .build();
    }

    private Property<String> propertyWithoutConversion;

    private String value = "current";

    private void whenReadingValueWithoutConversion() {
        valueWithoutConversion = propertyWithoutConversion.get();
    }

    private String valueWithoutConversion;

    private void thenValueWithoutConvertionIs(String value) {
        assertThat(valueWithoutConversion, is(value));
    }

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

    private void whenWritingValueWithoutConversion() {
        propertyWithoutConversion.set(newValue);
        valueWithoutConversion = propertyWithoutConversion.get();
    }

    private String newValue = "new";

    @Test
    public void readOnlyWithConversion() {
        givenReadOnlyPropertyWithConversion();
        whenReadingValueWithConversion();
        thenValueWithConvertionIs(new StringKey(value));
    }

    private void givenReadOnlyPropertyWithConversion() {
        propertyWithConversion = PropertyBuilder.simple(StringKey.class)
                .from(String.class)
                .adapt(StringKey::new)
                .get(() -> value)
                .build();
    }

    private Property<StringKey> propertyWithConversion;

    private void whenReadingValueWithConversion() {
        valueWithConversion = propertyWithConversion.get();
    }

    private StringKey valueWithConversion;

    private void thenValueWithConvertionIs(StringKey value) {
        assertThat(valueWithConversion, is(value));
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

    private void whenWritingValueWithConversion() {
        propertyWithConversion.set(new StringKey(newValue));
        valueWithConversion = propertyWithConversion.get();
    }
}
