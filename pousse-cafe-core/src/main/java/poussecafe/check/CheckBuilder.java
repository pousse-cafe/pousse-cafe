package poussecafe.check;

import java.util.Objects;
import java.util.function.Predicate;

public class CheckBuilder<T> {

    public CheckBuilder(T value) {
        this.specificiation = new AssertionSpecification<>(value);
    }

    private CheckSpecification<T> specificiation;

    public void notNull() {
        verifies(Objects::nonNull).run();
    }

    public CheckBuilder<T> verifies(Predicate<T> condition) {
        specificiation.verifies(condition);
        return this;
    }

    public void run() {
        new Check<>(specificiation).run();
    }
}
