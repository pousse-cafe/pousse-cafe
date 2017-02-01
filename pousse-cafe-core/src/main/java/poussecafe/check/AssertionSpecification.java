package poussecafe.check;

import poussecafe.exception.AssertionFailedException;

public class AssertionSpecification<T> extends CheckSpecification<T> {

    public static <T> AssertionSpecification<T> value(T value) {
        return new AssertionSpecification<>(value);
    }

    public AssertionSpecification(T value) {
        super(value);
    }

    @Override
    public void otherwise() {
        throw new AssertionFailedException(getMessage());
    }

}
