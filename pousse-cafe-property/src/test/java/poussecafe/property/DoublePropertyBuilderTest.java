package poussecafe.property;

import poussecafe.property.AddOperator;
import poussecafe.property.AddOperators;
import poussecafe.property.NumberPropertyBuilder;
import poussecafe.property.PropertyBuilder;

public class DoublePropertyBuilderTest extends NumberPropertyBuilderTest<Double> {

    @Override
    protected NumberPropertyBuilder<Double> builder() {
        return PropertyBuilder.number(Double.class);
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
