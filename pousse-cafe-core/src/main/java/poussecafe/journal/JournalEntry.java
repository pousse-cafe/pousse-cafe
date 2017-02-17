package poussecafe.journal;

import java.util.List;
import java.util.ListIterator;
import poussecafe.consequence.Consequence;
import poussecafe.process.ProcessManagerKey;
import poussecafe.storable.Storable;
import poussecafe.storable.StorableData;

import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.equalTo;
import static poussecafe.check.Predicates.not;
import static poussecafe.domain.DomainSpecification.value;

public class JournalEntry extends Storable<JournalEntryKey, JournalEntry.Data> {

    void setInitialStatus(JournalEntryStatus status) {
        checkThat(value(status).notNull().because("Status cannot be null"));
        getData().setStatus(status);
    }

    void setConsequence(Consequence consequence) {
        getData().setConsequence(consequence);
    }

    public void logSuccess() {
        logSuccess(null);
    }

    public void logSuccess(ProcessManagerKey createdProcessManagerKey) {
        if (createdProcessManagerKey != null) {
            getLogsWithNoSuccessDetected().add(JournalEntryLog.successLog(createdProcessManagerKey));
        } else {
            getLogsWithNoSuccessDetected().add(JournalEntryLog.successLog());
        }
        getData().setStatus(JournalEntryStatus.SUCCESS);
    }

    private List<JournalEntryLog> getLogsWithNoSuccessDetected() {
        checkThat(value(getStatus())
                .verifies(not(equalTo(JournalEntryStatus.SUCCESS)))
                .because("Entry can only have a single success log"));
        return getLogs();
    }

    public JournalEntryStatus getStatus() {
        return getData().getStatus();
    }

    public void logIgnored() {
        getLogs().add(JournalEntryLog.ignoreLog());
    }

    public void logFailure(String failureDescription) {
        getLogsWithNoSuccessDetected().add(JournalEntryLog.failureLog(failureDescription));
        getData().setStatus(JournalEntryStatus.FAILURE);
    }

    public List<JournalEntryLog> getLogs() {
        return getData().getLogs();
    }

    public JournalEntryLog getLastFailureLog() {
        ListIterator<JournalEntryLog> iterator = getLogs().listIterator(getLogs().size());
        while (iterator.hasPrevious()) {
            JournalEntryLog log = iterator.previous();
            if (log.getType() == JournalEntryLogType.FAILURE) {
                return log;
            }
        }
        return null;
    }

    public JournalEntryLog getSuccessLog() {
        return getLogs().stream().filter(log -> log.getType() == JournalEntryLogType.SUCCESS).findFirst().orElse(null);
    }

    public Consequence getConsequence() {
        return getData().getConsequence();
    }

    public static interface Data extends StorableData<JournalEntryKey> {

        void setConsequence(Consequence consequence);

        Consequence getConsequence();

        List<JournalEntryLog> getLogs();

        void setStatus(JournalEntryStatus status);

        JournalEntryStatus getStatus();
    }

}
