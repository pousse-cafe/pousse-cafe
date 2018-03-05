package poussecafe.journal;

import org.mockito.InOrder;

public class EntryCreatedOrUpdatedOnIgnoreTest extends EntryCreatedOrUpdatedTest {

    @Override
    protected void givenMessage() {
        givenIgnoredMessage();
    }

    @Override
    protected void whenLogging() {
        whenLoggingIgnoredConsumption();
    }

    @Override
    protected void verifyLog(InOrder sequence) {
        sequence.verify(existingEntry).logIgnored();
    }

}