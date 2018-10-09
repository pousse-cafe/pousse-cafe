package poussecafe.entity;

import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import poussecafe.property.PropertyBuilder;
import poussecafe.property.SetProperty;
import poussecafe.util.StringKey;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static poussecafe.collection.Collections.asSet;

public class SetPropertyBuilderTest {

    @Test
    public void readOnlyNoConversion() {
        givenReadOnlyPropertyWithoutConversion();
        whenReadingValueWithoutConversion();
        thenValueWithoutConvertionIs(value);
    }

    private void givenReadOnlyPropertyWithoutConversion() {
        propertyWithoutConversion = PropertyBuilder.set(String.class)
                .read()
                .build(value);
    }

    private SetProperty<String> propertyWithoutConversion;

    private Set<String> value = new HashSet<>(asList("current"));

    private void whenReadingValueWithoutConversion() {
        valueWithoutConversion = propertyWithoutConversion.get();
    }

    private Set<String> valueWithoutConversion;

    private void thenValueWithoutConvertionIs(Set<String> value) {
        assertThat(valueWithoutConversion, is(value));
    }

    @Test
    public void readWriteNoConversion() {
        givenReadWritePropertyWithoutConversion();
        whenWritingValueWithoutConversion();
        thenValueWithoutConvertionIs(newValue);
    }

    private void givenReadWritePropertyWithoutConversion() {
        propertyWithoutConversion = PropertyBuilder.set(String.class)
                .read()
                .write()
                .build(value);
    }

    private void whenWritingValueWithoutConversion() {
        propertyWithoutConversion.set(newValue);
        valueWithoutConversion = propertyWithoutConversion.get();
    }

    private Set<String> newValue = asSet("new");

    @Test
    public void readOnlyWithConversion() {
        givenReadOnlyPropertyWithConversion();
        whenReadingValueWithConversion();
        thenValueWithConvertionIs(value.stream().map(StringKey::new).collect(toSet()));
    }

    private void givenReadOnlyPropertyWithConversion() {
        propertyWithConversion = PropertyBuilder.set(StringKey.class)
                .from(String.class)
                .adapt(StringKey::new)
                .read()
                .build(value);
    }

    private SetProperty<StringKey> propertyWithConversion;

    private void whenReadingValueWithConversion() {
        valueWithConversion = propertyWithConversion.get();
    }

    private Set<StringKey> valueWithConversion;

    private void thenValueWithConvertionIs(Set<StringKey> value) {
        assertThat(valueWithConversion, is(value));
    }

    @Test
    public void readWriteWithConversion() {
        givenReadWritePropertyWithConversion();
        whenWritingValueWithConversion();
        thenValueWithConvertionIs(value.stream().map(StringKey::new).collect(toSet()));
    }

    private void givenReadWritePropertyWithConversion() {
        propertyWithConversion = PropertyBuilder.set(StringKey.class)
                .from(String.class)
                .adapt(StringKey::new)
                .read()
                .adapt(StringKey::getValue)
                .write()
                .build(value);
    }

    private void whenWritingValueWithConversion() {
        propertyWithConversion.set(newValue.stream().map(StringKey::new).collect(toSet()));
        valueWithConversion = propertyWithConversion.get();
    }
}
