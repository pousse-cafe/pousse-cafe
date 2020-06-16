package poussecafe.attribute.number;

import java.util.function.Consumer;
import java.util.function.Supplier;
import poussecafe.attribute.IntegerAttribute;

public class IntegerAttributeBuilder {

    public ExpectingWriter read(Supplier<Integer> getter) { // NOSONAR
        return new DefaultIntegerAttributeBuilder(getter);
    }

    public static interface ExpectingWriter {

        Complete write(Consumer<Integer> setter); // NOSONAR
    }

    public static interface Complete {

        IntegerAttribute build();
    }
}
