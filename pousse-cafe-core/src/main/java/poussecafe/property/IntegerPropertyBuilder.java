package poussecafe.property;

import java.util.function.Supplier;

public class IntegerPropertyBuilder {

    public ReadOnlyIntegerPropertyBuilder get(Supplier<Integer> getter) {
        return new ReadOnlyIntegerPropertyBuilder(getter);
    }
}
