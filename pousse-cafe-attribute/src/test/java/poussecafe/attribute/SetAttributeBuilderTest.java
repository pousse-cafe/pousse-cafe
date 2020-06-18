package poussecafe.attribute;

import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import poussecafe.util.StringId;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static poussecafe.collection.Collections.asSet;

public class SetAttributeBuilderTest {

    @Test
    public void readWriteNoConversion() {
        givenReadWriteAttributeWithoutConversion();
        whenWritingValueWithoutConversion();
        thenValueWithoutConvertionIs(newValue);
    }

    private void givenReadWriteAttributeWithoutConversion() {
        attributeWithoutConversion = AttributeBuilder.set(String.class)
                .withSet(value)
                .build();
    }

    private SetAttribute<String> attributeWithoutConversion;

    private Set<String> value = new HashSet<>(asList("current"));

    private void whenWritingValueWithoutConversion() {
        attributeWithoutConversion.value(newValue);
        valueWithoutConversion = attributeWithoutConversion.value();
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
        attributeWithConversion = AttributeBuilder.set(StringId.class)
                .itemsStoredAs(String.class)
                .adaptOnGet(StringId::new)
                .adaptOnSet(StringId::stringValue)
                .withSet(value)
                .build();
    }

    private SetAttribute<StringId> attributeWithConversion;

    private void whenWritingValueWithConversion() {
        attributeWithConversion.value(newValue.stream().map(StringId::new).collect(toSet()));
        valueWithConversion = attributeWithConversion.value();
    }

    private Set<StringId> valueWithConversion;

    private void thenValueWithConvertionIs(Set<StringId> value) {
        assertThat(valueWithConversion, is(value));
    }
}
