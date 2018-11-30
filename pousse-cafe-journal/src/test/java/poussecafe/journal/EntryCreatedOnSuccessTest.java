package poussecafe.journal;

public class EntryCreatedOnSuccessTest extends EntryCreatedTest {

    @Override
    protected void givenMessage() {
        givenSuccessfullyConsumedMessage();
    }

    @Override
    protected void whenLogging() {
        whenLoggingSuccessfulConsumption();
    }
}
