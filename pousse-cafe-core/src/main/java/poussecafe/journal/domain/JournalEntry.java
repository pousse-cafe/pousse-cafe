package poussecafe.journal.domain;

import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityData;
import poussecafe.journal.data.SerializedMessage;
import poussecafe.journal.data.SerializedMessage.Builder;
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

    void setSerializedMessage(SerializedMessage serializedMessage) {
        getData().setMessageId(serializedMessage.getId());
        getData().setMessageType(serializedMessage.getType());
        getData().setMessageData(serializedMessage.getData());
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

    public SerializedMessage getSerializedMessage() {
        return new SerializedMessage.Builder()
                .withId(getData().getMessageId())
                .withType(getData().getMessageType())
                .withData(getData().getMessageData())
                .build();
    }

    public static interface Data extends EntityData<JournalEntryKey> {

        void setMessageId(String messageId);

        String getMessageId();

        void setMessageType(String messageClassName);

        String getMessageType();

        void setMessageData(String messageData);

        String getMessageData();

        Property<Logs> logs();

        void setStatus(JournalEntryStatus status);

        JournalEntryStatus getStatus();
    }

}
