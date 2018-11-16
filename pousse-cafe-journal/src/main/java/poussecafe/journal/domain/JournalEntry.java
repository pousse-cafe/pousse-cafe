package poussecafe.journal.domain;

import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityData;
import poussecafe.property.Property;

import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.equalTo;
import static poussecafe.check.Predicates.not;
import static poussecafe.domain.DomainCheckSpecification.value;

public class JournalEntry extends AggregateRoot<JournalEntryKey, JournalEntry.Data> {

    void setInitialStatus(JournalEntryStatus status) {
        checkThat(value(status).notNull().because("Status cannot be null"));
        getData().setStatus(status);
    }

    void setSerializedMessage(String serializedMessage) {
        getData().setMessageData(serializedMessage);
    }

    public String getSerializedMessage() {
        return getData().getMessageData();
    }

    public void logSuccess() {
        getData().logs().set(getLogsWithNoSuccessDetected().withAdditional(JournalEntryLog.successLog()));
        getData().setStatus(JournalEntryStatus.SUCCESS);
    }

    private Logs getLogsWithNoSuccessDetected() {
        checkThat(value(getStatus())
                .verifies(not(equalTo(JournalEntryStatus.SUCCESS)))
                .because("Entry can only have a single success log"));
        return getLogs();
    }

    public JournalEntryStatus getStatus() {
        return getData().getStatus();
    }

    public void logIgnored() {
        getData().logs().set(getLogs().withAdditional(JournalEntryLog.ignoreLog()));
    }

    public void logFailure(String failureDescription) {
        getData().logs().set(getLogsWithNoSuccessDetected().withAdditional(JournalEntryLog.failureLog(failureDescription)));
        getData().setStatus(JournalEntryStatus.FAILURE);
    }

    public Logs getLogs() {
        return getData().logs().get();
    }

    public JournalEntryLog getLastFailureLog() {
        return getData().logs().get().getLastFailureLog();
    }

    public static interface Data extends EntityData<JournalEntryKey> {

        void setMessageData(String messageData);

        String getMessageData();

        Property<Logs> logs();

        void setStatus(JournalEntryStatus status);

        JournalEntryStatus getStatus();
    }

}
