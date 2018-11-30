package poussecafe.journal;

public class EntryCreatedOnFailureTest extends EntryCreatedTest {

    @Override
    protected void givenMessage() {
        givenMessageConsumptionFailed();
    }

    @Override
    protected void whenLogging() {
        whenLoggingFailedConsumption();
    }
}
