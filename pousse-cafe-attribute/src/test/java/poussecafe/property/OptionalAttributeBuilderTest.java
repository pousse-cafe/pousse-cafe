package poussecafe.property;

import java.util.Optional;
import org.junit.Test;
import poussecafe.attribute.OptionalAttribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.util.StringKey;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OptionalAttributeBuilderTest {

    private void thenValueWithoutConvertionIs(Optional<String> value) {
        assertThat(valueWithoutConversion, is(value));
    }

    @Test
    public void readWriteNoConversion() {
        givenReadWriteAttributeWithoutConversion();
        whenWritingValueWithoutConversion();
        thenValueWithoutConvertionIs(newValue);
    }

    private void givenReadWriteAttributeWithoutConversion() {
        propertyWithoutConversion = AttributeBuilder.optional(String.class)
                .get(() -> value)
                .set(newValue -> value = newValue)
                .build();
    }

    private OptionalAttribute<String> propertyWithoutConversion;

    private String value = "current";

    private void whenWritingValueWithoutConversion() {
        propertyWithoutConversion.value(newValue);
        valueWithoutConversion = propertyWithoutConversion.value();
    }

    private Optional<String> newValue = Optional.of("new");

    private OptionalAttribute<StringKey> propertyWithConversion;

    private Optional<String> valueWithoutConversion;

    private void thenValueWithConvertionIs(Optional<StringKey> value) {
        assertThat(valueWithConversion, is(value));
    }

    private Optional<StringKey> valueWithConversion;

    @Test
    public void readWriteWithConversion() {
        givenReadWriteAttributeWithConversion();
        whenWritingValueWithConversion();
        thenValueWithConvertionIs(newValue.map(StringKey::new));
    }

    private void givenReadWriteAttributeWithConversion() {
        propertyWithConversion = AttributeBuilder.optional(StringKey.class)
                .from(String.class)
                .adapt(StringKey::new)
                .get(() -> value)
                .adapt(StringKey::getValue)
                .set(newValue -> value = newValue)
                .build();
    }

    private void whenWritingValueWithConversion() {
        propertyWithConversion.value(newValue.map(StringKey::new));
        valueWithConversion = propertyWithConversion.value();
    }
}
