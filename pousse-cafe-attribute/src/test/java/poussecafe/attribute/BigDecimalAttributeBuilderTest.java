package poussecafe.attribute;

import java.math.BigDecimal;

public class BigDecimalAttributeBuilderTest extends NumberAttributeBuilderTest<BigDecimal> {

    @Override
    protected NumberAttributeBuilder<BigDecimal> builder() {
        return AttributeBuilder.number(BigDecimal.class);
    }

    @Override
    protected BigDecimal initialValue() {
        return new BigDecimal(42);
    }

    @Override
    protected AddOperator<BigDecimal> addOperator() {
        return AddOperators.BIG_DECIMAL;
    }

    @Override
    protected BigDecimal newValue() {
        return new BigDecimal(43);
    }

    @Override
    protected BigDecimal addTerm() {
        return new BigDecimal(1);
    }
}
