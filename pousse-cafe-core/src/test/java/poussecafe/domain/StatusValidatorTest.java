package poussecafe.domain;

import java.util.Optional;
import org.junit.Test;
import poussecafe.exception.RetryOperationException;
import poussecafe.exception.SameOperationException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class StatusValidatorTest {

    @Test
    public void validStatusPassesWithElseValidFalse() {
        givenValidator(Optional.of(false));
        whenChecking(Status.VALID);
        thenThrowsNothing();
    }

    private void givenValidator(Optional<Boolean> elseValid) {
        var builder = new StatusValidator.Builder<>(Status.class);
        if(elseValid.isPresent()) {
            builder.elseValid(elseValid.get());
        }
        if(elseValid.isPresent()
                && !elseValid.get().booleanValue()) {
            builder.valid(Status.VALID);
        }
        validator = builder
                .ignore(Status.IGNORED)
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
    public void ignoredStatusThrowsSameOperationExceptionWithElseValidFalse() {
        givenValidator(Optional.of(false));
        whenChecking(Status.IGNORED);
        thenThrows(SameOperationException.class);
    }

    private void thenThrows(Class<? extends Exception> exceptionClass) {
        assertThat(validationError, instanceOf(exceptionClass));
    }

    @Test
    public void unregisteredStatusThrowsIllegalArgumentExceptionWithElseValidFalse() {
        givenValidator(Optional.of(false));
        whenChecking(Status.UNREGISTERED);
        thenThrows(IllegalArgumentException.class);
    }

    @Test
    public void retriedStatusThrowsSameOperationExceptionWithElseValidFalse() {
        givenValidator(Optional.of(false));
        whenChecking(Status.RETRIED);
        thenThrows(RetryOperationException.class);
    }

    @Test
    public void unregisteredStatusPassesByDefault() {
        givenValidator(Optional.empty());
        whenChecking(Status.UNREGISTERED);
        thenThrowsNothing();
    }

    @Test
    public void validStatusPassesByDefault() {
        givenValidator(Optional.empty());
        whenChecking(Status.VALID);
        thenThrowsNothing();
    }

    @Test
    public void ignoredStatusThrowsSameOperationExceptionByDefault() {
        givenValidator(Optional.empty());
        whenChecking(Status.IGNORED);
        thenThrows(SameOperationException.class);
    }

    @Test
    public void retriedStatusThrowsSameOperationExceptionByDefault() {
        givenValidator(Optional.empty());
        whenChecking(Status.RETRIED);
        thenThrows(RetryOperationException.class);
    }
    
    @Test
    public void validStatusPassesWithElseValidTrue() {
        givenValidator(Optional.of(true));
        whenChecking(Status.VALID);
        thenThrowsNothing();
    }

    @Test
    public void ignoredStatusThrowsSameOperationExceptionWithElseValidTrue() {
        givenValidator(Optional.of(true));
        whenChecking(Status.IGNORED);
        thenThrows(SameOperationException.class);
    }

    @Test
    public void retriedStatusThrowsSameOperationExceptionWithElseValidTrue() {
        givenValidator(Optional.of(true));
        whenChecking(Status.RETRIED);
        thenThrows(RetryOperationException.class);
    }

    @Test
    public void unregisteredStatusPassesWithElseValidTrue() {
        givenValidator(Optional.of(true));
        whenChecking(Status.UNREGISTERED);
        thenThrowsNothing();
    }
}
