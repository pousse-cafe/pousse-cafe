package poussecafe.check;

import java.util.Objects;
import java.util.function.Predicate;

public abstract class CheckSpecification<T> {

    private T value;

    private Predicate<T> predicate;

    private String message;

    public CheckSpecification(T value) {
        this.value = value;
    }

    public CheckSpecification<T> verifies(Predicate<T> matcher) {
        this.predicate = matcher;
        return this;
    }

    public CheckSpecification<T> notNull() {
        verifies(Objects::nonNull);
        return because("Value cannot be null");
    }

    public CheckSpecification<T> isTrue() {
        verifies(value -> Boolean.TRUE.equals(value));
        return because("Value must be true");
    }

    public CheckSpecification<T> because(String message) {
        this.message = message;
        return this;
    }

    public T getValue() {
        return value;
    }

    public Predicate<T> getPredicate() {
        return predicate;
    }

    public String getMessage() {
        return message;
    }

    public abstract void otherwise();

}
