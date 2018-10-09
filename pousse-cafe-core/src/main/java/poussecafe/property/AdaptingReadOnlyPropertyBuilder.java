package poussecafe.property;

import java.util.function.Function;
import java.util.function.Supplier;

import static poussecafe.check.Checks.checkThatValue;

/**
 * @param <U> Stored type
 * @param <T> Property type
 */
public class AdaptingReadOnlyPropertyBuilder<U, T> {

    AdaptingReadOnlyPropertyBuilder(Function<U, T> adapter) {
        this.adapter = adapter;
    }

    private Function<U, T> adapter;

    public AdaptedReadOnlyPropertyBuilder<U, T> get(Supplier<U> getter) {
        checkThatValue(getter).notNull();
        return new AdaptedReadOnlyPropertyBuilder<>(() -> adapter.apply(getter.get()));
    }
}
