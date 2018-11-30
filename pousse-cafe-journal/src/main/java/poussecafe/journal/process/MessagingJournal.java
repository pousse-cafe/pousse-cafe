package poussecafe.journal.process;

import poussecafe.events.FailedConsumption;
import poussecafe.events.SuccessfulConsumption;
import poussecafe.journal.domain.JournalEntry;
import poussecafe.journal.domain.JournalEntryFactory;
import poussecafe.journal.domain.JournalEntryKey;
import poussecafe.journal.domain.JournalEntryRepository;
import poussecafe.messaging.DomainEventListener;
import poussecafe.process.DomainProcess;

public class MessagingJournal extends DomainProcess {

    private JournalEntryRepository entryRepository;

    private JournalEntryFactory entryFactory;

    @DomainEventListener
    public void logSuccessfulConsumption(SuccessfulConsumption consumption) {
        JournalEntrySaver saver = buildSaver(consumption.consumptionId().get(), consumption.listenerId().get(), consumption.rawMessage().get());
        runInTransaction(JournalEntry.class, () -> {
            JournalEntry entry = saver.findOrBuild();
            entry.logSuccess();
            saver.save();
        });
    }

    private JournalEntrySaver buildSaver(
            String consumptionId,
            String listenerId,
            String rawMessage) {
        JournalEntrySaver saver = new JournalEntrySaver();
        saver.setMessage(rawMessage);
        saver.setEntryFactory(entryFactory);
        saver.setEntryRepository(entryRepository);

        JournalEntryKey entryKey = new JournalEntryKey(consumptionId, listenerId);
        saver.setEntryKey(entryKey);

        return saver;
    }

    @DomainEventListener
    public void logFailedConsumption(FailedConsumption event) {
        JournalEntrySaver saver = buildSaver(event.consumptionId().get(), event.listenerId().get(), event.rawMessage().get());
        runInTransaction(JournalEntry.class, () -> {
            JournalEntry entry = saver.findOrBuild();
            entry.logFailure(event.error().get());
            saver.save();
        });
    }
}
