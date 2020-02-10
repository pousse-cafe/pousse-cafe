package poussecafe.attribute;

import org.junit.Test;
import poussecafe.attribute.AddOperator;
import poussecafe.attribute.NumberAttribute;
import poussecafe.attribute.NumberAttributeBuilder;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public abstract class NumberAttributeBuilderTest<N extends Number> {

    private N readValue;

    private void thenReadValueIs(N value) {
        assertThat(readValue, is(value));
    }

    @Test
    public void readWriteNoConversion() {
        givenReadWriteAttribute();
        whenWritingValueWithoutConversion();
        thenReadValueIs(newValue);
    }

    private void givenReadWriteAttribute() {
        property = builder()
                .get(() -> value)
                .write(newValue -> value = newValue)
                .addOperator(addOperator())
                .build();
    }

    protected abstract NumberAttributeBuilder<N> builder();

    private N value = initialValue();

    protected abstract N initialValue();

    protected abstract AddOperator<N> addOperator();

    private NumberAttribute<N> property;

    private void whenWritingValueWithoutConversion() {
        property.value(newValue);
        readValue = property.value();
    }

    private N newValue = newValue();

    protected abstract N newValue();

    @Test
    public void add() {
        givenReadWriteAttribute();
        N addTerm = addTerm();
        whenAdding(addTerm);
        thenReadValueIs(addOperator().add(valueBeforeAdd, addTerm));
    }

    protected abstract N addTerm();

    private void whenAdding(N term) {
        valueBeforeAdd = value;
        property.add(term);
        readValue = property.value();
    }

    private N valueBeforeAdd;
}
