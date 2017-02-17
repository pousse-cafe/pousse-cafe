package poussecafe.journal;

import org.junit.Test;
import poussecafe.consequence.Consequence;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SingleConsequenceReplayTest extends ConsequenceReplayTest {

    private Consequence consequence;

    @Test
    public void failedConsequenceIsRoutedOnReplay() {
        givenFailedConsequence();
        whenReplayingConsequence();
        thenConsequenceIsRouted();
    }

    private void givenFailedConsequence() {
        givenConsequence();
        givenAtLeastOneFailedConsumption();
    }

    private void givenConsequence() {
        consequence = consequenceWithId("consequenceId");
    }

    private void givenAtLeastOneFailedConsumption() {
        JournalEntry successEntry = entryWithStatus(JournalEntryStatus.SUCCESS, consequence);
        JournalEntry inProgressEntry = entryWithStatus(JournalEntryStatus.IN_PROGRESS, consequence);
        JournalEntry failedEntry = entryWithStatus(JournalEntryStatus.FAILURE, consequence);
        when(journalEntryRepository.findByConsequenceId(consequence.getId()))
        .thenReturn(asList(successEntry, inProgressEntry, failedEntry));
    }

    private void whenReplayingConsequence() {
        consequenceReplayer.replayConsequence(consequence.getId());
    }

    private void thenConsequenceIsRouted() {
        verify(consequenceRouter).routeConsequence(consequence);
    }

    @Test
    public void successConsequenceIsNotRoutedOnReplay() {
        givenNoFailedConsequence();
        whenReplayingConsequence();
        thenConsequenceIsNotRouted();
    }

    private void givenNoFailedConsequence() {
        givenConsequence();
        givenNoFailedConsumption();
    }

    private void givenNoFailedConsumption() {
        JournalEntry successEntry = entryWithStatus(JournalEntryStatus.SUCCESS, consequence);
        JournalEntry inProgressEntry = entryWithStatus(JournalEntryStatus.IN_PROGRESS, consequence);
        when(journalEntryRepository.findByConsequenceId(consequence.getId()))
        .thenReturn(asList(successEntry, inProgressEntry));
    }

    private void thenConsequenceIsNotRouted() {
        thenConsequenceIsNotRouted(consequence);
    }
}
