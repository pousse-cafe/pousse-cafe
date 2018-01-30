package poussecafe.journal;

import java.util.List;
import java.util.ListIterator;
import poussecafe.storable.IdentifiedStorable;
import poussecafe.storable.Property;
import poussecafe.storable.StorableData;

import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.equalTo;
import static poussecafe.check.Predicates.not;
import static poussecafe.domain.DomainSpecification.value;

public class JournalEntry extends IdentifiedStorable<JournalEntryKey, JournalEntry.Data> {

    @Override
    public JournalEntryKey getKey() {
        return new JournalEntryKey(getData().getMessageId(), getData().listenerId().get());
    }

    @Override
    public void setKey(JournalEntryKey key) {
        getData().setMessageId(key.getMessageId());
        getData().listenerId().set(key.getListenerId());
    }

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
        getLogsWithNoSuccessDetected().add(JournalEntryLog.successLog());
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

    public SerializedMessage getSerializedMessage() {
        return new SerializedMessage.Builder()
                .withId(getData().getMessageId())
                .withType(getData().getMessageType())
                .withData(getData().getMessageData())
                .build();
    }

    public static interface Data extends StorableData {

        Property<String> listenerId();

        void setMessageId(String messageId);

        String getMessageId();

        void setMessageType(String messageClassName);

        String getMessageType();

        void setMessageData(String messageData);

        String getMessageData();

        List<JournalEntryLog> getLogs();

        void setStatus(JournalEntryStatus status);

        JournalEntryStatus getStatus();
    }

}
