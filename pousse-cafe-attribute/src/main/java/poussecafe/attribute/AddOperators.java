package poussecafe.attribute;

import java.math.BigDecimal;

public class AddOperators {

    private AddOperators() {

    }

    public static final AddOperator<Integer> INTEGER = (x, y) -> x + y;

    public static final AddOperator<BigDecimal> BIG_DECIMAL = (x, y) -> x.add(y);

    public static final AddOperator<Double> DOUBLE = (x, y) -> x + y;
}
