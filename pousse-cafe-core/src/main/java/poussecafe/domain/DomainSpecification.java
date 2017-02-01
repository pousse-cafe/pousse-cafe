package poussecafe.domain;

import poussecafe.check.CheckSpecification;

public class DomainSpecification<T> extends CheckSpecification<T> {

    public static <T> DomainSpecification<T> value(T value) {
        return new DomainSpecification<>(value);
    }

    public DomainSpecification(T value) {
        super(value);
    }

    @Override
    public void otherwise() {
        throw new DomainException(getMessage());
    }

}
