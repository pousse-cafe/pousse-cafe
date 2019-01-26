package poussecafe.property;

import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import poussecafe.util.StringKey;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static poussecafe.collection.Collections.asSet;

public class SetPropertyBuilderTest {

    @Test
    public void readWriteNoConversion() {
        givenReadWritePropertyWithoutConversion();
        whenWritingValueWithoutConversion();
        thenValueWithoutConvertionIs(newValue);
    }

    private void givenReadWritePropertyWithoutConversion() {
        propertyWithoutConversion = PropertyBuilder.set(String.class)
                .withSet(value)
                .build();
    }

    private SetProperty<String> propertyWithoutConversion;

    private Set<String> value = new HashSet<>(asList("current"));

    private void whenWritingValueWithoutConversion() {
        propertyWithoutConversion.set(newValue);
        valueWithoutConversion = propertyWithoutConversion.get();
    }

    private Set<String> newValue = asSet("new");

    private Set<String> valueWithoutConversion;

    private void thenValueWithoutConvertionIs(Set<String> value) {
        assertThat(valueWithoutConversion, is(value));
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
                .adaptOnGet(StringKey::new)
                .adaptOnSet(StringKey::getValue)
                .withSet(value)
                .build();
    }

    private SetProperty<StringKey> propertyWithConversion;

    private void whenWritingValueWithConversion() {
        propertyWithConversion.set(newValue.stream().map(StringKey::new).collect(toSet()));
        valueWithConversion = propertyWithConversion.get();
    }

    private Set<StringKey> valueWithConversion;

    private void thenValueWithConvertionIs(Set<StringKey> value) {
        assertThat(valueWithConversion, is(value));
    }
}
