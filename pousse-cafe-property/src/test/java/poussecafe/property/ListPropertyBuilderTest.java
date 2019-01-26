package poussecafe.property;

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
    public void readWriteNoConversion() {
        givenReadWritePropertyWithoutConversion();
        whenWritingValueWithoutConversion();
        thenValueWithoutConvertionIs(newValue);
    }

    private void givenReadWritePropertyWithoutConversion() {
        propertyWithoutConversion = PropertyBuilder.list(String.class)
                .withList(value)
                .build();
    }

    private ListProperty<String> propertyWithoutConversion;

    private List<String> value = new ArrayList<>(asList("current"));

    private void whenWritingValueWithoutConversion() {
        propertyWithoutConversion.set(newValue);
        valueWithoutConversion = propertyWithoutConversion.get();
    }

    private List<String> newValue = asList("new");

    private List<String> valueWithoutConversion;

    private void thenValueWithoutConvertionIs(List<String> value) {
        assertThat(valueWithoutConversion, is(value));
    }

    private ListProperty<StringKey> propertyWithConversion;

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
                .adaptOnGet(StringKey::new)
                .adaptOnSet(StringKey::getValue)
                .withList(value)
                .build();
    }

    private void whenWritingValueWithConversion() {
        propertyWithConversion.set(newValue.stream().map(StringKey::new).collect(toList()));
        valueWithConversion = propertyWithConversion.get();
    }
}
