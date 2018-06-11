package poussecafe.storable;

import java.math.BigDecimal;

public abstract class BigDecimalProperty implements NumberProperty<BigDecimal> {

    @Override
    public void add(BigDecimal term) {
        set(get().add(term));
    }
}
