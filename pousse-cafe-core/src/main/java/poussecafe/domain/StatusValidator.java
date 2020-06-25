package poussecafe.domain;

import java.util.EnumSet;
import java.util.Set;
import poussecafe.exception.RetryOperationException;
import poussecafe.exception.SameOperationException;

public class StatusValidator<E extends Enum<E>> {

    public static class Builder<F extends Enum<F>> {

        public Builder(Class<F> enumClass) {
            this.enumClass = enumClass;
            validator = new StatusValidator<>(enumClass);
        }

        private Class<F> enumClass;

        private StatusValidator<F> validator;

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

        public Builder<F> elseValid(boolean elseValid) {
            this.elseValid = elseValid;
            return this;
        }

        private boolean elseValid = true;

        public StatusValidator<F> build() {
            if(elseValid) {
                validator.valid = EnumSet.allOf(enumClass);
                validator.valid.removeAll(validator.ignore);
                validator.valid.removeAll(validator.retry);
            }
            return validator;
        }
    }

    private StatusValidator(Class<E> enumClass) {
        valid = EnumSet.noneOf(enumClass);
        ignore = EnumSet.noneOf(enumClass);
        retry = EnumSet.noneOf(enumClass);
    }

    private Set<E> valid;

    private Set<E> ignore;

    private Set<E> retry;

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
