package poussecafe.journal.process;

import poussecafe.journal.data.SerializedMessage;
import poussecafe.journal.domain.JournalEntry;
import poussecafe.journal.domain.JournalEntryFactory;
import poussecafe.journal.domain.JournalEntryKey;
import poussecafe.journal.domain.JournalEntryRepository;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

class JournalEntrySaver {

    private JournalEntryKey entryKey;

    private SerializedMessage message;

    private JournalEntryRepository entryRepository;

    private JournalEntryFactory entryFactory;

    private JournalEntry entry;

    private boolean entryFound;

    public JournalEntry findOrBuild() {
        entry = entryRepository.find(entryKey);
        if (entry == null) {
            entryFound = false;
            entry = entryFactory.buildEntryForSentMessage(entryKey, message);
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

    public void setMessage(SerializedMessage message) {
        checkThat(value(message).notNull().because("Message cannot be null"));
        this.message = message;
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
