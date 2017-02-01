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
        Entry entry = mock(Entry.class);
        when(entry.hasLogWithType(EntryLogType.SUCCESS)).thenReturn(successLog);
        when(entryRepository.find(new EntryKey(consequence.getId(), listenerId))).thenReturn(entry);
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
