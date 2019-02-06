package poussecafe.attribute;

import java.util.function.Supplier;

public class IntegerAttributeBuilder {

    public ReadOnlyIntegerAttributeBuilder get(Supplier<Integer> getter) {
        return new ReadOnlyIntegerAttributeBuilder(getter);
    }
}
