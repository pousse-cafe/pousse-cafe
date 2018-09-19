package poussecafe.storable;

import java.util.function.Function;
import java.util.function.Supplier;

import static poussecafe.check.Checks.checkThatValue;

/**
 * @param <U> Stored type
 * @param <T> Property type
 */
public class AdaptingReadOnlyNumberPropertyBuilder<U, T> {

    AdaptingReadOnlyNumberPropertyBuilder(Function<U, T> adapter) {
        this.adapter = adapter;
    }

    private Function<U, T> adapter;

    public AdaptedReadOnlyOptionalPropertyBuilder<U, T> get(Supplier<U> getter) {
        checkThatValue(getter).notNull();
        return new AdaptedReadOnlyOptionalPropertyBuilder<>(() -> adapter.apply(getter.get()));
    }
}
