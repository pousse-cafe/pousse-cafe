package poussecafe.journal;

import poussecafe.consequence.Consequence;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

class JournalEntrySaver {

    private JournalEntryKey entryKey;

    private Consequence consequence;

    private JournalEntryRepository entryRepository;

    private JournalEntryFactory entryFactory;

    private JournalEntry entry;

    private boolean entryFound;

    public JournalEntry findOrBuild() {
        entry = entryRepository.find(entryKey);
        if (entry == null) {
            entryFound = false;
            entry = entryFactory.buildEntryForEmittedConsequence(entryKey, consequence);
        } else {
            entryFound = true;
        }
        return entry;
    }

    public void save() {
        if (entryFound) {
            entryRepository.update(entry);
        } else {
            entryRepository.add(entry);
        }
    }

    public void setEntryKey(JournalEntryKey entryKey) {
        checkThat(value(entryKey).notNull().because("Entry key cannot be null"));
        this.entryKey = entryKey;
    }

    public void setConsequence(Consequence consequence) {
        checkThat(value(consequence).notNull().because("Consequence cannot be null"));
        this.consequence = consequence;
    }

    public void setEntryRepository(JournalEntryRepository entryRepository) {
        checkThat(value(entryRepository).notNull().because("Entry repository cannot be null"));
        this.entryRepository = entryRepository;
    }

    public void setEntryFactory(JournalEntryFactory entryFactory) {
        checkThat(value(entryFactory).notNull().because("Entry factory cannot be null"));
        this.entryFactory = entryFactory;
    }

}
