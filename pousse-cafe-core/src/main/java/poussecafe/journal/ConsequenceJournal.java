package poussecafe.journal;

import java.util.List;
import poussecafe.consequence.Consequence;
import poussecafe.exception.AssertionFailedException;
import poussecafe.service.TransactionAwareService;
import poussecafe.util.ExceptionUtils;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class ConsequenceJournal extends TransactionAwareService {

    private EntryRepository entryRepository;

    private EntryFactory entryFactory;

    public void logSuccessfulConsumption(String listenerId,
            Consequence consequence) {
        EntrySaver saver = buildSaver(listenerId, consequence);
        runInTransaction(() -> {
            Entry entry = saver.findOrBuild();
            entry.logSuccess();
            saver.save();
        });
    }

    private EntrySaver buildSaver(String listenerId,
            Consequence consequence) {
        EntrySaver saver = new EntrySaver();
        saver.setConsequence(consequence);
        saver.setEntryFactory(entryFactory);
        saver.setEntryRepository(entryRepository);

        EntryKey entryKey = new EntryKey(consequence.getId(), listenerId);
        saver.setEntryKey(entryKey);

        return saver;
    }

    public void logIgnoredConsumption(String listenerId,
            Consequence consequence) {
        EntrySaver saver = buildSaver(listenerId, consequence);
        runInTransaction(() -> {
            Entry entry = saver.findOrBuild();
            entry.logIgnored();
            saver.save();
        });
    }

    public void logFailedConsumption(String listenerId,
            Consequence consequence,
            Exception e) {
        EntrySaver saver = buildSaver(listenerId, consequence);
        runInTransaction(() -> {
            Entry entry = saver.findOrBuild();
            entry.logFailure(ExceptionUtils.getStackTrace(e));
            saver.save();
        });
    }

    public boolean isSuccessfullyConsumed(Consequence consequence,
            String listenerId) {
        Entry entry = entryRepository
                .find(new EntryKey(consequence.getId(), listenerId));
        if (entry == null) {
            return false;
        } else {
            return entry.hasLogWithType(EntryLogType.SUCCESS);
        }
    }

    public Entry findCommandEntry(String commandId) {
        List<Entry> entries = entryRepository.findByConsequenceId(commandId);
        if (entries.size() > 1) {
            throw new AssertionFailedException("Consequence was consumed by several listeners, cannot be a command");
        }
        if (entries.size() == 1) {
            return entries.get(0);
        } else {
            return null;
        }
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
