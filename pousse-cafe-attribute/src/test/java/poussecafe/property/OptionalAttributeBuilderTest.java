package poussecafe.property;

import java.util.Optional;
import org.junit.Test;
import poussecafe.attribute.OptionalAttribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.util.StringId;

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

    private OptionalAttribute<StringId> propertyWithConversion;

    private Optional<String> valueWithoutConversion;

    private void thenValueWithConvertionIs(Optional<StringId> value) {
        assertThat(valueWithConversion, is(value));
    }

    private Optional<StringId> valueWithConversion;

    @Test
    public void readWriteWithConversion() {
        givenReadWriteAttributeWithConversion();
        whenWritingValueWithConversion();
        thenValueWithConvertionIs(newValue.map(StringId::new));
    }

    private void givenReadWriteAttributeWithConversion() {
        propertyWithConversion = AttributeBuilder.optional(StringId.class)
                .from(String.class)
                .adapt(StringId::new)
                .get(() -> value)
                .adapt(StringId::getValue)
                .set(newValue -> value = newValue)
                .build();
    }

    private void whenWritingValueWithConversion() {
        propertyWithConversion.value(newValue.map(StringId::new));
        valueWithConversion = propertyWithConversion.value();
    }
}
