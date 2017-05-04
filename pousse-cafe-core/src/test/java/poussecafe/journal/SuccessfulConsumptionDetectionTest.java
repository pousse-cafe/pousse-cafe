package poussecafe.journal;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SuccessfulConsumptionDetectionTest extends MessagingJournalTest {

    private boolean consumptionSuccess;

    @Test
    public void successfulConsumptionIsDetectedWithExistingEntryAndSuccessLogged() {
        givenConfiguredMessagingJournal();
        givenLoggedSuccessfulConsumption();
        whenDetectingSuccessfulConsumption();
        thenConsumptionSuccessIs(true);
    }

    private void givenLoggedSuccessfulConsumption() {
        givenReceivedMessage();
        givenExistingEntryWithSuccessLogValue(true);
    }

    protected void givenExistingEntryWithSuccessLogValue(boolean successLog) {
        JournalEntry entry = mock(JournalEntry.class);
        when(entry.getStatus()).thenReturn(successLog ? JournalEntryStatus.SUCCESS : JournalEntryStatus.FAILURE);
        when(entryRepository.find(new JournalEntryKey(message.getId(), listenerId))).thenReturn(entry);
    }

    private void whenDetectingSuccessfulConsumption() {
        consumptionSuccess = journal.isSuccessfullyConsumed(message, listenerId);
    }

    private void thenConsumptionSuccessIs(boolean expected) {
        assertThat(consumptionSuccess, is(expected));
    }

    @Test
    public void unsuccessfulConsumptionIsDetectedWithNoEntry() {
        givenConfiguredMessagingJournal();
        givenReceivedMessage();
        whenDetectingSuccessfulConsumption();
        thenConsumptionSuccessIs(false);
    }

    @Test
    public void unsuccessfulConsumptionIsDetectedWithExistingEntryAndNoSuccess() {
        givenConfiguredMessagingJournal();
        givenLoggedUnsuccessfulConsumption();
        whenDetectingSuccessfulConsumption();
        thenConsumptionSuccessIs(false);
    }

    private void givenLoggedUnsuccessfulConsumption() {
        givenReceivedMessage();
        givenExistingEntryWithSuccessLogValue(false);
    }
}
