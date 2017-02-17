package poussecafe.journal;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import poussecafe.consequence.Consequence;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AllConsequencesReplayTest extends ConsequenceReplayTest {

    private List<JournalEntry> failedEntries;

    @Test
    public void allFailedReplayedRouted() {
        givenFailedConsequences();
        whenReplayAll();
        thenFailedReplayed();
    }

    private void givenFailedConsequences() {
        failedEntries = new ArrayList<>();
        failedEntries.add(failedConsequenceEntry("id1"));
        failedEntries.add(failedConsequenceEntry("id2"));
        failedEntries.add(failedConsequenceEntry("id3"));
        when(journalEntryRepository.findByStatus(JournalEntryStatus.FAILURE)).thenReturn(failedEntries);
    }

    protected JournalEntry failedConsequenceEntry(String consequenceId) {
        Consequence consequence = consequenceWithId(consequenceId);
        JournalEntry entry = entryWithStatus(JournalEntryStatus.FAILURE, consequence);
        return entry;
    }

    private void whenReplayAll() {
        consequenceReplayer.replayAllFailedConsequences();
    }

    private void thenFailedReplayed() {
        for (JournalEntry failedEntry : failedEntries) {
            verify(consequenceRouter).routeConsequence(failedEntry.getConsequence());
        }
    }
}
