package poussecafe.domain;

import java.util.HashSet;
import java.util.Set;
import poussecafe.exception.RetryOperationException;
import poussecafe.exception.SameOperationException;

public class StatusValidator<E extends Enum<E>> {

    public static class Builder<F extends Enum<F>> {

        private StatusValidator<F> validator = new StatusValidator<>();

        public Builder<F> valid(F status) {
            validator.valid.add(status);
            return this;
        }

        public Builder<F> ignore(F status) {
            validator.ignore.add(status);
            return this;
        }

        public Builder<F> retry(F status) {
            validator.retry.add(status);
            return this;
        }

        public StatusValidator<F> build() {
            return validator;
        }
    }

    private StatusValidator() {

    }

    private Set<E> valid = new HashSet<>();

    private Set<E> ignore = new HashSet<>();

    private Set<E> retry = new HashSet<>();

    public void validOrElseThrow(E current) {
        if(ignore.contains(current)) {
            throw new SameOperationException();
        } else if(retry.contains(current)) {
            throw new RetryOperationException();
        } else if(!valid.contains(current)) {
            throw new IllegalArgumentException("Unexpected status " + current);
        } else {
            // !ignore.contains(current) && valid.contains(current) -> OK
        }
    }
}
