package poussecafe.journal;

import java.util.List;
import java.util.ListIterator;
import poussecafe.consequence.Consequence;
import poussecafe.storable.Storable;
import poussecafe.storable.StorableData;

import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.not;
import static poussecafe.domain.DomainSpecification.value;
import static poussecafe.journal.HasLog.hasLog;

public class Entry extends Storable<EntryKey, Entry.Data> {

    void setConsequence(Consequence consequence) {
        getData().setConsequence(consequence);
    }

    public void logSuccess() {
        getLogsWithNoSuccessDetected().add(EntryLog.successLog());
    }

    private List<EntryLog> getLogsWithNoSuccessDetected() {
        List<EntryLog> logs = getLogs();
        checkThat(value(logs)
                .verifies(not(hasLog(EntryLogType.SUCCESS)))
                .because("Entry can only have a single success log"));
        return logs;
    }

    public boolean hasLogWithType(EntryLogType type) {
        List<EntryLog> logs = getLogs();
        return hasLog(type).test(logs);
    }

    public void logIgnored() {
        getLogs().add(EntryLog.ignoreLog());
    }

    public void logFailure(String failureDescription) {
        getLogsWithNoSuccessDetected().add(EntryLog.failureLog(failureDescription));
    }

    public List<EntryLog> getLogs() {
        return getData().getLogs();
    }

    public EntryLog getLastFailureLog() {
        ListIterator<EntryLog> iterator = getLogs().listIterator(getLogs().size());
        while (iterator.hasPrevious()) {
            EntryLog log = iterator.previous();
            if (log.getType() == EntryLogType.FAILURE) {
                return log;
            }
        }
        return null;
    }

    public static interface Data extends StorableData<EntryKey> {

        void setConsequence(Consequence consequence);

        Consequence getConsequence();

        List<EntryLog> getLogs();
    }

}
