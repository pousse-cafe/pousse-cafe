package poussecafe.journal;

import poussecafe.consequence.Consequence;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

class EntrySaver {

    private EntryKey entryKey;

    private Consequence consequence;

    private EntryRepository entryRepository;

    private EntryFactory entryFactory;

    private Entry entry;

    private boolean entryFound;

    public Entry findOrBuild() {
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

    public void setEntryKey(EntryKey entryKey) {
        checkThat(value(entryKey).notNull().because("Entry key cannot be null"));
        this.entryKey = entryKey;
    }

    public void setConsequence(Consequence consequence) {
        checkThat(value(consequence).notNull().because("Consequence cannot be null"));
        this.consequence = consequence;
    }

    public void setEntryRepository(EntryRepository entryRepository) {
        checkThat(value(entryRepository).notNull().because("Entry repository cannot be null"));
        this.entryRepository = entryRepository;
    }

    public void setEntryFactory(EntryFactory entryFactory) {
        checkThat(value(entryFactory).notNull().because("Entry factory cannot be null"));
        this.entryFactory = entryFactory;
    }

}
