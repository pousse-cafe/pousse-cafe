package poussecafe.journal;

import org.mockito.InOrder;
import poussecafe.util.ExceptionUtils;

public class EntryCreatedOrUpdatedOnFailureTest extends EntryCreatedOrUpdatedTest {

    @Override
    protected void givenMessage() {
        givenMessageConsumptionFailed();
    }

    @Override
    protected void whenLogging() {
        whenLoggingFailedConsumption();
    }

    @Override
    protected void verifyLog(InOrder sequence) {
        sequence.verify(existingEntry).logFailure(ExceptionUtils.getStackTrace(exception));
    }

}
