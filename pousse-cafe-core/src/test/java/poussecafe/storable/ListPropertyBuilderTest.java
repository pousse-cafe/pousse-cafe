package poussecafe.storable;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import poussecafe.util.StringKey;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ListPropertyBuilderTest {

    @Test
    public void readOnlyNoConversion() {
        givenReadOnlyPropertyWithoutConversion();
        whenReadingValueWithoutConversion();
        thenValueWithoutConvertionIs(value);
    }

    private void givenReadOnlyPropertyWithoutConversion() {
        propertyWithoutConversion = PropertyBuilder.list(String.class)
                .read()
                .build(value);
    }

    private ListProperty<String> propertyWithoutConversion;

    private List<String> value = new ArrayList<>(asList("current"));

    private void whenReadingValueWithoutConversion() {
        valueWithoutConversion = propertyWithoutConversion.get();
    }

    private List<String> valueWithoutConversion;

    private void thenValueWithoutConvertionIs(List<String> value) {
        assertThat(valueWithoutConversion, is(value));
    }

    @Test
    public void readWriteNoConversion() {
        givenReadWritePropertyWithoutConversion();
        whenWritingValueWithoutConversion();
        thenValueWithoutConvertionIs(newValue);
    }

    private void givenReadWritePropertyWithoutConversion() {
        propertyWithoutConversion = PropertyBuilder.list(String.class)
                .read()
                .write()
                .build(value);
    }

    private void whenWritingValueWithoutConversion() {
        propertyWithoutConversion.set(newValue);
        valueWithoutConversion = propertyWithoutConversion.get();
    }

    private List<String> newValue = asList("new");

    @Test
    public void readOnlyWithConversion() {
        givenReadOnlyPropertyWithConversion();
        whenReadingValueWithConversion();
        thenValueWithConvertionIs(value.stream().map(StringKey::new).collect(toList()));
    }

    private void givenReadOnlyPropertyWithConversion() {
        propertyWithConversion = PropertyBuilder.list(StringKey.class)
                .from(String.class)
                .adapt(StringKey::new)
                .read()
                .build(value);
    }

    private ListProperty<StringKey> propertyWithConversion;

    private void whenReadingValueWithConversion() {
        valueWithConversion = propertyWithConversion.get();
    }

    private List<StringKey> valueWithConversion;

    private void thenValueWithConvertionIs(List<StringKey> value) {
        assertThat(valueWithConversion, is(value));
    }

    @Test
    public void readWriteWithConversion() {
        givenReadWritePropertyWithConversion();
        whenWritingValueWithConversion();
        thenValueWithConvertionIs(valueWithConversion);
    }

    private void givenReadWritePropertyWithConversion() {
        propertyWithConversion = PropertyBuilder.list(StringKey.class)
                .from(String.class)
                .adapt(StringKey::new)
                .read()
                .adapt(StringKey::getValue)
                .write()
                .build(value);
    }

    private void whenWritingValueWithConversion() {
        propertyWithConversion.set(newValue.stream().map(StringKey::new).collect(toList()));
        valueWithConversion = propertyWithConversion.get();
    }
}
