package poussecafe.attribute;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class IntegerAttributeBuilder {

    public ExpectingWriter read(Supplier<Integer> getter) {
        return new DefaultIntegerAttributeBuilder(getter);
    }

    public static interface ExpectingWriter {

        Complete write(Consumer<Integer> setter);
    }

    public static interface Complete {

        IntegerAttribute build();
    }
}
