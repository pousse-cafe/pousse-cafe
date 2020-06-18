package poussecafe.attribute;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import poussecafe.util.StringId;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ListAttributeBuilderTest {

    @Test
    public void readWriteNoConversion() {
        givenReadWriteAttributeWithoutConversion();
        whenWritingValueWithoutConversion();
        thenValueWithoutConvertionIs(newValue);
    }

    private void givenReadWriteAttributeWithoutConversion() {
        attributeWithoutConversion = AttributeBuilder.list(String.class)
                .withList(value)
                .build();
    }

    private ListAttribute<String> attributeWithoutConversion;

    private List<String> value = new ArrayList<>(asList("current"));

    private void whenWritingValueWithoutConversion() {
        attributeWithoutConversion.value(newValue);
        valueWithoutConversion = attributeWithoutConversion.value();
    }

    private List<String> newValue = asList("new");

    private List<String> valueWithoutConversion;

    private void thenValueWithoutConvertionIs(List<String> value) {
        assertThat(valueWithoutConversion, is(value));
    }

    private ListAttribute<StringId> attributeWithConversion;

    private List<StringId> valueWithConversion;

    private void thenValueWithConvertionIs(List<StringId> value) {
        assertThat(valueWithConversion, is(value));
    }

    @Test
    public void readWriteWithConversion() {
        givenReadWriteAttributeWithConversion();
        whenWritingValueWithConversion();
        thenValueWithConvertionIs(valueWithConversion);
    }

    private void givenReadWriteAttributeWithConversion() {
        attributeWithConversion = AttributeBuilder.list(StringId.class)
                .itemsStoredAs(String.class)
                .adaptOnGet(StringId::new)
                .adaptOnSet(StringId::stringValue)
                .withList(value)
                .build();
    }

    private void whenWritingValueWithConversion() {
        attributeWithConversion.value(newValue.stream().map(StringId::new).collect(toList()));
        valueWithConversion = attributeWithConversion.value();
    }
}
