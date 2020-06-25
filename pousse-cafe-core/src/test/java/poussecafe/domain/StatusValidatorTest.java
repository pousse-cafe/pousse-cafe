package poussecafe.domain;

import org.junit.Test;
import poussecafe.exception.RetryOperationException;
import poussecafe.exception.SameOperationException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class StatusValidatorTest {

    @Test
    public void validStatusPasses() {
        givenValidator();
        whenChecking(Status.VALID);
        thenThrowsNothing();
    }

    private void givenValidator() {
        validator = new StatusValidator.Builder<Status>()
                .ignore(Status.IGNORED)
                .valid(Status.VALID)
                .retry(Status.RETRIED)
                .build();
    }

    private StatusValidator<Status> validator;

    private void whenChecking(Status status) {
        try {
            validator.validOrElseThrow(status);
        } catch (Exception e) {
            validationError = e;
        }
    }

    private Exception validationError;

    private void thenThrowsNothing() {
        assertThat(validationError, nullValue());
    }

    @Test
    public void ignoredStatusThrowsSameOperationException() {
        givenValidator();
        whenChecking(Status.IGNORED);
        thenThrows(SameOperationException.class);
    }

    private void thenThrows(Class<? extends Exception> exceptionClass) {
        assertThat(validationError, instanceOf(exceptionClass));
    }

    @Test
    public void invalidStatusThrowsIllegalArgumentException() {
        givenValidator();
        whenChecking(Status.INVALID);
        thenThrows(IllegalArgumentException.class);
    }

    @Test
    public void retriedStatusThrowsSameOperationException() {
        givenValidator();
        whenChecking(Status.RETRIED);
        thenThrows(RetryOperationException.class);
    }
}
