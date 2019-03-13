package poussecafe.journal.process;

import poussecafe.discovery.MessageListener;
import poussecafe.journal.domain.JournalEntry;
import poussecafe.journal.domain.JournalEntryFactory;
import poussecafe.journal.domain.JournalEntryRepository;
import poussecafe.process.DomainProcess;
import poussecafe.support.model.FailedConsumption;
import poussecafe.support.model.SuccessfulConsumption;

public class StoreConsumptionResult extends DomainProcess {

    private JournalEntryRepository entryRepository;

    private JournalEntryFactory entryFactory;

    @MessageListener
    public void storeSuccessfulConsumption(SuccessfulConsumption event) {
        JournalEntry entry = entryFactory.buildEntry(event);
        runInTransaction(JournalEntry.class, () -> entryRepository.add(entry));
    }

    @MessageListener
    public void storeFailedConsumption(FailedConsumption event) {
        JournalEntry entry = entryFactory.buildEntry(event);
        runInTransaction(JournalEntry.class, () -> entryRepository.add(entry));
    }
}
