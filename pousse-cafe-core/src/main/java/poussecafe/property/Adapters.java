package poussecafe.property;

import java.util.function.Function;

public class Adapters {

    private Adapters() {

    }

    public static <T> Function<T, T> identity() {
        return value -> value;
    }
}
