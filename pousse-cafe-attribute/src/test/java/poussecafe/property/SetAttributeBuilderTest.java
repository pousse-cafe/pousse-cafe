package poussecafe.property;

import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.attribute.SetAttribute;
import poussecafe.util.StringId;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static poussecafe.collection.Collections.asSet;

public class SetAttributeBuilderTest {

    @Test
    public void readWriteNoConversion() {
        givenReadWriteAttributeWithoutConversion();
        whenWritingValueWithoutConversion();
        thenValueWithoutConvertionIs(newValue);
    }

    private void givenReadWriteAttributeWithoutConversion() {
        propertyWithoutConversion = AttributeBuilder.set(String.class)
                .withSet(value)
                .build();
    }

    private SetAttribute<String> propertyWithoutConversion;

    private Set<String> value = new HashSet<>(asList("current"));

    private void whenWritingValueWithoutConversion() {
        propertyWithoutConversion.value(newValue);
        valueWithoutConversion = propertyWithoutConversion.value();
    }

    private Set<String> newValue = asSet("new");

    private Set<String> valueWithoutConversion;

    private void thenValueWithoutConvertionIs(Set<String> value) {
        assertThat(valueWithoutConversion, is(value));
    }

    @Test
    public void readWriteWithConversion() {
        givenReadWriteAttributeWithConversion();
        whenWritingValueWithConversion();
        thenValueWithConvertionIs(value.stream().map(StringId::new).collect(toSet()));
    }

    private void givenReadWriteAttributeWithConversion() {
        propertyWithConversion = AttributeBuilder.set(StringId.class)
                .itemsStoredAs(String.class)
                .adaptOnGet(StringId::new)
                .adaptOnSet(StringId::stringValue)
                .withSet(value)
                .build();
    }

    private SetAttribute<StringId> propertyWithConversion;

    private void whenWritingValueWithConversion() {
        propertyWithConversion.value(newValue.stream().map(StringId::new).collect(toSet()));
        valueWithConversion = propertyWithConversion.value();
    }

    private Set<StringId> valueWithConversion;

    private void thenValueWithConvertionIs(Set<StringId> value) {
        assertThat(valueWithConversion, is(value));
    }
}
