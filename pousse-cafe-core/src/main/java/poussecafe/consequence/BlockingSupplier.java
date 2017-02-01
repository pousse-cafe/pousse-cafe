package poussecafe.consequence;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Supplier;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class BlockingSupplier<T> implements Supplier<T> {

    public static final Duration DEFAULT_TIME_OUT = Duration.ofSeconds(30);

    private Function<Duration, T> function;

    public BlockingSupplier(Function<Duration, T> function) {
        setFunction(function);
    }

    private void setFunction(Function<Duration, T> function) {
        checkThat(value(function).notNull().because("Function cannot be null"));
        this.function = function;
    }

    @Override
    public T get() {
        return get(DEFAULT_TIME_OUT);
    }

    public T get(Duration timeOut) {
        return function.apply(timeOut);
    }

}
