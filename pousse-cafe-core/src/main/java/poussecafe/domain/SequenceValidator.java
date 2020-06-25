package poussecafe.domain;

import poussecafe.exception.RetryOperationException;
import poussecafe.exception.SameOperationException;

public class SequenceValidator {

    public static SequenceValidator receivedSequenceNumber(long receivedSequenceNumber) {
        SequenceValidator validator = new SequenceValidator();
        validator.receivedSequenceNumber = receivedSequenceNumber;
        return validator;
    }

    private long receivedSequenceNumber;

    public void followsOrThrow(long currentSequenceNumber) {
        if(currentSequenceNumber >= receivedSequenceNumber) {
            throw new SameOperationException();
        } else if(currentSequenceNumber < receivedSequenceNumber - 1) {
            throw new RetryOperationException();
        }
    }

    private SequenceValidator() {

    }
}
