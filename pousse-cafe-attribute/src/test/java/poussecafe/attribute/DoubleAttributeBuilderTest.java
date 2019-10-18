package poussecafe.attribute;

import poussecafe.attribute.AddOperator;
import poussecafe.attribute.AddOperators;
import poussecafe.attribute.NumberAttributeBuilder;
import poussecafe.attribute.AttributeBuilder;

public class DoubleAttributeBuilderTest extends NumberAttributeBuilderTest<Double> {

    @Override
    protected NumberAttributeBuilder<Double> builder() {
        return AttributeBuilder.number(Double.class);
    }

    @Override
    protected Double initialValue() {
        return 42.;
    }

    @Override
    protected AddOperator<Double> addOperator() {
        return AddOperators.DOUBLE;
    }

    @Override
    protected Double newValue() {
        return 43.;
    }

    @Override
    protected Double addTerm() {
        return 1.;
    }
}
