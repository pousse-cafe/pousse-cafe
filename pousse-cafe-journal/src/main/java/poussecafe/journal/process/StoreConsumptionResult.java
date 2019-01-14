package poussecafe.journal.process;

import poussecafe.journal.domain.JournalEntry;
import poussecafe.journal.domain.JournalEntryFactory;
import poussecafe.journal.domain.JournalEntryRepository;
import poussecafe.messaging.DomainEventListener;
import poussecafe.process.DomainProcess;
import poussecafe.support.model.FailedConsumption;
import poussecafe.support.model.SuccessfulConsumption;

public class StoreConsumptionResult extends DomainProcess {

    private JournalEntryRepository entryRepository;

    private JournalEntryFactory entryFactory;

    @DomainEventListener
    public void storeSuccessfulConsumption(SuccessfulConsumption event) {
        JournalEntry entry = entryFactory.buildEntry(event);
        runInTransaction(JournalEntry.class, () -> entryRepository.add(entry));
    }

    @DomainEventListener
    public void storeFailedConsumption(FailedConsumption event) {
        JournalEntry entry = entryFactory.buildEntry(event);
        runInTransaction(JournalEntry.class, () -> entryRepository.add(entry));
    }
}
