package poussecafe.attribute;

public class IntegerAttributeBuilderTest extends NumberAttributeBuilderTest<Integer> {

    @Override
    protected NumberAttributeBuilder<Integer> builder() {
        return AttributeBuilder.number(Integer.class);
    }

    @Override
    protected Integer initialValue() {
        return 42;
    }

    @Override
    protected AddOperator<Integer> addOperator() {
        return AddOperators.INTEGER;
    }

    @Override
    protected Integer newValue() {
        return 43;
    }

    @Override
    protected Integer addTerm() {
        return 1;
    }
}
