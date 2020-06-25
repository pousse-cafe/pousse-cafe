package poussecafe.domain;

import org.junit.Test;
import poussecafe.exception.RetryOperationException;
import poussecafe.exception.SameOperationException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class SequenceValidatorTest {

    @Test
    public void nextInSequencePasses() {
        givenCurrent(1);
        givenNext(2);
        whenValidating();
        thenThrowsNothing();
    }

    private void givenCurrent(long value) {
        current = value;
    }

    private long current;

    private void givenNext(long value) {
        next = value;
    }

    private long next;

    private void whenValidating() {
        try {
            SequenceValidator.receivedSequenceNumber(next).followsOrThrow(current);
        } catch (Exception e) {
            validationError = e;
        }
    }

    private Exception validationError;

    private void thenThrowsNothing() {
        assertThat(validationError, nullValue());
    }

    @Test
    public void gapInSequenceThrowsRetryOperationException() {
        givenCurrent(1);
        givenNext(3);
        whenValidating();
        thenThrows(RetryOperationException.class);
    }

    private void thenThrows(Class<? extends Exception> exceptionClass) {
        assertThat(validationError, instanceOf(exceptionClass));
    }

    @Test
    public void pastInSequenceThrowsSameOperationException() {
        givenCurrent(2);
        givenNext(1);
        whenValidating();
        thenThrows(SameOperationException.class);
    }
}
