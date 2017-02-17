package poussecafe.journal;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SuccessfulConsumptionDetectionTest extends ConsequenceJournalTest {

    private boolean consumptionSuccess;

    @Test
    public void successfulConsumptionIsDetectedWithExistingEntryAndSuccessLogged() {
        givenConfiguredConsequenceJournal();
        givenLoggedSuccessfulConsumption();
        whenDetectingSuccessfulConsumption();
        thenConsumptionSuccessIs(true);
    }

    private void givenLoggedSuccessfulConsumption() {
        givenReceivedConsequence();
        givenExistingEntryWithSuccessLogValue(true);
    }

    protected void givenExistingEntryWithSuccessLogValue(boolean successLog) {
        JournalEntry entry = mock(JournalEntry.class);
        when(entry.getStatus()).thenReturn(successLog ? JournalEntryStatus.SUCCESS : JournalEntryStatus.FAILURE);
        when(entryRepository.find(new JournalEntryKey(consequence.getId(), listenerId))).thenReturn(entry);
    }

    private void whenDetectingSuccessfulConsumption() {
        consumptionSuccess = journal.isSuccessfullyConsumed(consequence, listenerId);
    }

    private void thenConsumptionSuccessIs(boolean expected) {
        assertThat(consumptionSuccess, is(expected));
    }

    @Test
    public void unsuccessfulConsumptionIsDetectedWithNoEntry() {
        givenConfiguredConsequenceJournal();
        givenReceivedConsequence();
        whenDetectingSuccessfulConsumption();
        thenConsumptionSuccessIs(false);
    }

    @Test
    public void unsuccessfulConsumptionIsDetectedWithExistingEntryAndNoSuccess() {
        givenConfiguredConsequenceJournal();
        givenLoggedUnsuccessfulConsumption();
        whenDetectingSuccessfulConsumption();
        thenConsumptionSuccessIs(false);
    }

    private void givenLoggedUnsuccessfulConsumption() {
        givenReceivedConsequence();
        givenExistingEntryWithSuccessLogValue(false);
    }
}
