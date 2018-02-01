package poussecafe.journal;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.exception.AssertionFailedException;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageAdapter;
import poussecafe.service.TransactionAwareService;
import poussecafe.util.ExceptionUtils;

public class MessagingJournal extends TransactionAwareService {

    private MessageAdapter messageAdapter;

    private JournalEntryRepository entryRepository;

    private JournalEntryFactory entryFactory;

    public void logSuccessfulConsumption(String listenerId,
            SuccessfulConsumption consumption) {
        JournalEntrySaver saver = buildSaver(listenerId, consumption.getConsumedMessage());
        runInTransaction(JournalEntry.class, () -> {
            JournalEntry entry = saver.findOrBuild();
            entry.logSuccess();
            saver.save();
        });
    }

    private JournalEntrySaver buildSaver(String listenerId,
            Message message) {
        JournalEntrySaver saver = new JournalEntrySaver();
        saver.setMessage(messageAdapter.adaptMessage(message));
        saver.setEntryFactory(entryFactory);
        saver.setEntryRepository(entryRepository);

        JournalEntryKey entryKey = new JournalEntryKey(message.getId(), listenerId);
        saver.setEntryKey(entryKey);

        return saver;
    }

    public void logIgnoredConsumption(String listenerId,
            Message message) {
        JournalEntrySaver saver = buildSaver(listenerId, message);
        runInTransaction(JournalEntry.class, () -> {
            JournalEntry entry = saver.findOrBuild();
            entry.logIgnored();
            saver.save();
        });
    }

    public void logFailedConsumption(String listenerId,
            Message message,
            Exception e) {
        logger.error("Consumption of consequence {} by listener {} failed", message, listenerId, e);
        JournalEntrySaver saver = buildSaver(listenerId, message);
        runInTransaction(JournalEntry.class, () -> {
            JournalEntry entry = saver.findOrBuild();
            entry.logFailure(ExceptionUtils.getStackTrace(e));
            saver.save();
        });
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    public boolean isSuccessfullyConsumed(Message message,
            String listenerId) {
        JournalEntryKey entryKey = new JournalEntryKey(message.getId(), listenerId);
        JournalEntry entry = entryRepository.find(entryKey);
        if (entry == null) {
            return false;
        } else {
            return entry.getStatus() == JournalEntryStatus.SUCCESS;
        }
    }

    public JournalEntry findCommandEntry(String commandId) {
        List<JournalEntry> entries = entryRepository.findByMessageId(commandId);
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
