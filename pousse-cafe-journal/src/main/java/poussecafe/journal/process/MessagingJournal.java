package poussecafe.journal.process;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.exception.AssertionFailedException;
import poussecafe.journal.JacksonMessageAdapter;
import poussecafe.journal.domain.JournalEntry;
import poussecafe.journal.domain.JournalEntryFactory;
import poussecafe.journal.domain.JournalEntryKey;
import poussecafe.journal.domain.JournalEntryRepository;
import poussecafe.messaging.DomainEventListener;
import poussecafe.messaging.FailedConsumption;
import poussecafe.messaging.Message;
import poussecafe.messaging.SuccessfulConsumption;
import poussecafe.process.DomainProcess;

public class MessagingJournal extends DomainProcess {

    private JournalEntryRepository entryRepository;

    private JournalEntryFactory entryFactory;

    @DomainEventListener
    public void logSuccessfulConsumption(SuccessfulConsumption consumption) {
        JournalEntrySaver saver = buildSaver(consumption.getConsumptionId(), consumption.getListenerId(), consumption.getConsumedMessage());
        runInTransaction(JournalEntry.class, () -> {
            JournalEntry entry = saver.findOrBuild();
            entry.logSuccess();
            saver.save();
        });
    }

    private JournalEntrySaver buildSaver(
            String consumptionId,
            String listenerId,
            Message message) {
        JournalEntrySaver saver = new JournalEntrySaver();
        saver.setMessage(jacksonMessageAdapter.adaptMessage(message));
        saver.setEntryFactory(entryFactory);
        saver.setEntryRepository(entryRepository);

        JournalEntryKey entryKey = new JournalEntryKey(consumptionId, listenerId);
        saver.setEntryKey(entryKey);

        return saver;
    }

    private JacksonMessageAdapter jacksonMessageAdapter = new JacksonMessageAdapter();

    @DomainEventListener
    public void logFailedConsumption(FailedConsumption event) {
        logger.error("Consumption of message {} by listener {} failed", event.getConsumedMessage(), event.getListenerId(), event.getError());
        JournalEntrySaver saver = buildSaver(event.getConsumptionId(), event.getListenerId(), event.getConsumedMessage());
        runInTransaction(JournalEntry.class, () -> {
            JournalEntry entry = saver.findOrBuild();
            entry.logFailure(event.getError());
            saver.save();
        });
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    public JournalEntry findCommandEntry(String commandId) {
        List<JournalEntry> entries = entryRepository.findByConsumptionId(commandId);
        if (entries.size() > 1) {
            throw new AssertionFailedException("Message was consumed by several listeners, cannot be a command");
        }
        if (entries.size() == 1) {
            return entries.get(0);
        } else {
            return null;
        }
    }

}
