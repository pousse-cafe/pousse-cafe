package poussecafe.property;

import org.junit.Test;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.util.StringKey;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SimpleAttributeBuilderTest {

    @Test
    public void readWriteNoConversion() {
        givenReadWriteAttributeWithoutConversion();
        whenWritingValueWithoutConversion();
        thenValueWithoutConvertionIs(newValue);
    }

    private void givenReadWriteAttributeWithoutConversion() {
        propertyWithoutConversion = AttributeBuilder.single(String.class)
                .get(() -> value)
                .set(newValue -> value = newValue)
                .build();
    }

    private Attribute<String> propertyWithoutConversion;

    private String value = "current";

    private void whenWritingValueWithoutConversion() {
        propertyWithoutConversion.value(newValue);
        valueWithoutConversion = propertyWithoutConversion.value();
    }

    private String newValue = "new";

    private String valueWithoutConversion;

    private void thenValueWithoutConvertionIs(String value) {
        assertThat(valueWithoutConversion, is(value));
    }

    @Test
    public void readWriteWithConversion() {
        givenReadWriteAttributeWithConversion();
        whenWritingValueWithConversion();
        thenValueWithConvertionIs(new StringKey(newValue));
    }

    private void givenReadWriteAttributeWithConversion() {
        propertyWithConversion = AttributeBuilder.single(StringKey.class)
                .from(String.class)
                .adapt(StringKey::new)
                .get(() -> value)
                .adapt(StringKey::getValue)
                .set(newValue -> value = newValue)
                .build();
    }

    private Attribute<StringKey> propertyWithConversion;

    private void whenWritingValueWithConversion() {
        propertyWithConversion.value(new StringKey(newValue));
        valueWithConversion = propertyWithConversion.value();
    }

    private StringKey valueWithConversion;

    private void thenValueWithConvertionIs(StringKey value) {
        assertThat(valueWithConversion, is(value));
    }
}
