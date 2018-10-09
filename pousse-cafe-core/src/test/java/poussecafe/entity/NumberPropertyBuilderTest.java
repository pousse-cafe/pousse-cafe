package poussecafe.entity;

import org.junit.Test;
import poussecafe.property.AddOperator;
import poussecafe.property.NumberProperty;
import poussecafe.property.NumberPropertyBuilder;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public abstract class NumberPropertyBuilderTest<N extends Number> {

    @Test
    public void readOnlyNoConversion() {
        givenReadOnlyPropertyWithoutConversion();
        whenReadingValueWithoutConversion();
        thenReadValueIs(value);
    }

    private void givenReadOnlyPropertyWithoutConversion() {
        property = builder()
                .get(() -> value)
                .build();
    }

    protected abstract NumberPropertyBuilder<N> builder();

    private NumberProperty<N> property;

    private N value = initialValue();

    protected abstract N initialValue();

    private void whenReadingValueWithoutConversion() {
        readValue = property.get();
    }

    private N readValue;

    private void thenReadValueIs(N value) {
        assertThat(readValue, is(value));
    }

    @Test
    public void readWriteNoConversion() {
        givenReadWriteProperty();
        whenWritingValueWithoutConversion();
        thenReadValueIs(newValue);
    }

    private void givenReadWriteProperty() {
        property = builder()
                .get(() -> value)
                .set(newValue -> value = newValue)
                .addOperator(addOperator())
                .build();
    }

    protected abstract AddOperator<N> addOperator();

    private void whenWritingValueWithoutConversion() {
        property.set(newValue);
        readValue = property.get();
    }

    private N newValue = newValue();

    protected abstract N newValue();

    @Test
    public void add() {
        givenReadWriteProperty();
        N addTerm = addTerm();
        whenAdding(addTerm);
        thenReadValueIs(addOperator().add(valueBeforeAdd, addTerm));
    }

    protected abstract N addTerm();

    private void whenAdding(N term) {
        valueBeforeAdd = value;
        property.add(term);
        readValue = property.get();
    }

    private N valueBeforeAdd;
}
