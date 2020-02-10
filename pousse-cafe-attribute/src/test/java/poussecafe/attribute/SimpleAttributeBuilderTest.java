package poussecafe.attribute;

import org.junit.Test;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.util.StringId;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SimpleAttributeBuilderTest {

    @Test
    public void readWriteNoConversion() {
        givenReadWriteAttributeWithoutConversion();
        whenWritingValueWithoutConversion();
        thenValueWithoutConvertionIs(newValue);
    }

    private void givenReadWriteAttributeWithoutConversion() {
        propertyWithoutConversion = AttributeBuilder.single(String.class)
                .read(() -> value)
                .write(newValue -> value = newValue)
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
        thenValueWithConvertionIs(new StringId(newValue));
    }

    private void givenReadWriteAttributeWithConversion() {
        propertyWithConversion = AttributeBuilder.single(StringId.class)
                .storedAs(String.class)
                .adaptOnRead(StringId::new)
                .read(() -> value)
                .adaptOnWrite(StringId::stringValue)
                .write(newValue -> value = newValue)
                .build();
    }

    private Attribute<StringId> propertyWithConversion;

    private void whenWritingValueWithConversion() {
        propertyWithConversion.value(new StringId(newValue));
        valueWithConversion = propertyWithConversion.value();
    }

    private StringId valueWithConversion;

    private void thenValueWithConvertionIs(StringId value) {
        assertThat(valueWithConversion, is(value));
    }
}
