package poussecafe.domain;

import poussecafe.check.CheckSpecification;

public class DomainCheckSpecification<T> extends CheckSpecification<T> {

    public static <T> DomainCheckSpecification<T> value(T value) {
        return new DomainCheckSpecification<>(value);
    }

    public DomainCheckSpecification(T value) {
        super(value);
    }

    @Override
    public void otherwise() {
        throw new DomainException(getMessage());
    }

}
