package poussecafe.journal;

import org.mockito.InOrder;

public class EntryCreatedOrUpdatedOnSuccessTest extends EntryCreatedOrUpdatedTest {

    @Override
    protected void givenConsequence() {
        givenSuccessfullyConsumedConsequence();
    }

    @Override
    protected void whenLogging() {
        whenLoggingSuccessfulConsumption();
    }

    @Override
    protected void verifyLog(InOrder sequence) {
        sequence.verify(existingEntry).logSuccess();
    }

}
