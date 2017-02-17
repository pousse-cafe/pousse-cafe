package poussecafe.journal;

import java.util.List;
import java.util.Set;
import poussecafe.consequence.Consequence;
import poussecafe.consequence.ConsequenceRouter;

import static java.util.stream.Collectors.toSet;
import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class ConsequenceReplayer {

    private JournalEntryRepository journalEntryRepository;

    private ConsequenceRouter consequenceRouter;

    public void replayConsequence(String consequenceId) {
        List<JournalEntry> entries = journalEntryRepository.findByConsequenceId(consequenceId);
        replayConsequenceOfEntries(entries);
    }

    private void replayConsequenceOfEntries(List<JournalEntry> entries) {
        replayConsequences(uniqueConsequencesOfFailedEntries(entries));
    }

    private Set<Consequence> uniqueConsequencesOfFailedEntries(List<JournalEntry> entries) {
        return entries
                .stream()
                .filter(entry -> entry.getStatus() == JournalEntryStatus.FAILURE)
                .map(JournalEntry::getConsequence)
                .collect(toSet());
    }

    private void replayConsequences(Set<Consequence> consequences) {
        for (Consequence consequence : consequences) {
            consequenceRouter.routeConsequence(consequence);
        }
    }

    public void replayAllFailedConsequences() {
        List<JournalEntry> failedEntries = journalEntryRepository.findByStatus(JournalEntryStatus.FAILURE);
        replayConsequenceOfEntries(failedEntries);

    }

    public void setJournalEntryRepository(JournalEntryRepository journalEntryRepository) {
        checkThat(value(journalEntryRepository).notNull().because("Journal entry repository cannot be null"));
        this.journalEntryRepository = journalEntryRepository;
    }

    public void setConsequenceRouter(ConsequenceRouter consequenceRouter) {
        checkThat(value(consequenceRouter).notNull().because("Consequence router cannot be null"));
        this.consequenceRouter = consequenceRouter;
    }
}
