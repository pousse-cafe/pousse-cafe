package poussecafe.journal;

import java.util.List;
import poussecafe.consequence.Consequence;
import poussecafe.exception.AssertionFailedException;
import poussecafe.service.TransactionAwareService;
import poussecafe.util.ExceptionUtils;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class ConsequenceJournal extends TransactionAwareService {

    private JournalEntryRepository entryRepository;

    private JournalEntryFactory entryFactory;

    public void logSuccessfulConsumption(String listenerId,
            SuccessfulConsumption consumption) {
        JournalEntrySaver saver = buildSaver(listenerId, consumption.getConsumedConsequence());
        runInTransaction(JournalEntry.Data.class, () -> {
            JournalEntry entry = saver.findOrBuild();
            if (consumption.hasCreatedProcessManagerKey()) {
                entry.logSuccess(consumption.getCreatedProcessManagerKey());
            } else {
                entry.logSuccess();
            }
            saver.save();
        });
    }

    private JournalEntrySaver buildSaver(String listenerId,
            Consequence consequence) {
        JournalEntrySaver saver = new JournalEntrySaver();
        saver.setConsequence(consequence);
        saver.setEntryFactory(entryFactory);
        saver.setEntryRepository(entryRepository);

        JournalEntryKey entryKey = new JournalEntryKey(consequence.getId(), listenerId);
        saver.setEntryKey(entryKey);

        return saver;
    }

    public void logIgnoredConsumption(String listenerId,
            Consequence consequence) {
        JournalEntrySaver saver = buildSaver(listenerId, consequence);
        runInTransaction(JournalEntry.Data.class, () -> {
            JournalEntry entry = saver.findOrBuild();
            entry.logIgnored();
            saver.save();
        });
    }

    public void logFailedConsumption(String listenerId,
            Consequence consequence,
            Exception e) {
        JournalEntrySaver saver = buildSaver(listenerId, consequence);
        runInTransaction(JournalEntry.Data.class, () -> {
            JournalEntry entry = saver.findOrBuild();
            entry.logFailure(ExceptionUtils.getStackTrace(e));
            saver.save();
        });
    }

    public boolean isSuccessfullyConsumed(Consequence consequence,
            String listenerId) {
        JournalEntry entry = entryRepository
                .find(new JournalEntryKey(consequence.getId(), listenerId));
        if (entry == null) {
            return false;
        } else {
            return entry.getStatus() == JournalEntryStatus.SUCCESS;
        }
    }

    public JournalEntry findCommandEntry(String commandId) {
        List<JournalEntry> entries = entryRepository.findByConsequenceId(commandId);
        if (entries.size() > 1) {
            throw new AssertionFailedException("Consequence was consumed by several listeners, cannot be a command");
        }
        if (entries.size() == 1) {
            return entries.get(0);
        } else {
            return null;
        }
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
