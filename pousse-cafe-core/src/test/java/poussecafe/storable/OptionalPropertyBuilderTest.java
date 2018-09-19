package poussecafe.storable;

import java.util.Optional;
import org.junit.Test;
import poussecafe.util.StringKey;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OptionalPropertyBuilderTest {

    @Test
    public void readOnlyNoConversion() {
        givenReadOnlyPropertyWithoutConversion();
        whenReadingValueWithoutConversion();
        thenValueWithoutConvertionIs(Optional.of(value));
    }

    private void givenReadOnlyPropertyWithoutConversion() {
        propertyWithoutConversion = PropertyBuilder.optional(String.class)
                .get(() -> value)
                .build();
    }

    private OptionalProperty<String> propertyWithoutConversion;

    private String value = "current";

    private void whenReadingValueWithoutConversion() {
        valueWithoutConversion = propertyWithoutConversion.get();
    }

    private Optional<String> valueWithoutConversion;

    private void thenValueWithoutConvertionIs(Optional<String> value) {
        assertThat(valueWithoutConversion, is(value));
    }

    @Test
    public void readWriteNoConversion() {
        givenReadWritePropertyWithoutConversion();
        whenWritingValueWithoutConversion();
        thenValueWithoutConvertionIs(newValue);
    }

    private void givenReadWritePropertyWithoutConversion() {
        propertyWithoutConversion = PropertyBuilder.optional(String.class)
                .get(() -> value)
                .set(newValue -> value = newValue)
                .build();
    }

    private void whenWritingValueWithoutConversion() {
        propertyWithoutConversion.set(newValue);
        valueWithoutConversion = propertyWithoutConversion.get();
    }

    private Optional<String> newValue = Optional.of("new");

    @Test
    public void readOnlyWithConversion() {
        givenReadOnlyPropertyWithConversion();
        whenReadingValueWithConversion();
        thenValueWithConvertionIs(Optional.of(new StringKey(value)));
    }

    private void givenReadOnlyPropertyWithConversion() {
        propertyWithConversion = PropertyBuilder.optional(StringKey.class)
                .from(String.class)
                .adapt(StringKey::new)
                .get(() -> value)
                .build();
    }

    private OptionalProperty<StringKey> propertyWithConversion;

    private void whenReadingValueWithConversion() {
        valueWithConversion = propertyWithConversion.get();
    }

    private Optional<StringKey> valueWithConversion;

    private void thenValueWithConvertionIs(Optional<StringKey> value) {
        assertThat(valueWithConversion, is(value));
    }

    @Test
    public void readWriteWithConversion() {
        givenReadWritePropertyWithConversion();
        whenWritingValueWithConversion();
        thenValueWithConvertionIs(newValue.map(StringKey::new));
    }

    private void givenReadWritePropertyWithConversion() {
        propertyWithConversion = PropertyBuilder.optional(StringKey.class)
                .from(String.class)
                .adapt(StringKey::new)
                .get(() -> value)
                .adapt(StringKey::getValue)
                .set(newValue -> value = newValue)
                .build();
    }

    private void whenWritingValueWithConversion() {
        propertyWithConversion.set(newValue.map(StringKey::new));
        valueWithConversion = propertyWithConversion.get();
    }
}
