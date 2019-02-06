package poussecafe.attribute;

import java.math.BigDecimal;

public abstract class BigDecimalAttribute implements NumberAttribute<BigDecimal> {

    @Override
    public void add(BigDecimal term) {
        value(value().add(term));
    }
}
