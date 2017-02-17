package poussecafe.journal;

import java.time.LocalDateTime;
import poussecafe.process.ProcessManagerKey;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class JournalEntryLog {

    private LocalDateTime dateTime;

    private JournalEntryLogType type;

    private String description;

    private ProcessManagerKey createdProcessManagerKey;

    private JournalEntryLog(LocalDateTime dateTime, JournalEntryLogType type, String description,
            ProcessManagerKey createdProcessManagerKey) {
        setDateTime(dateTime);
        setType(type);
        setDescription(description);
        setCreatedProcessManagerKey(createdProcessManagerKey);
    }

    public static JournalEntryLog successLog() {
        return successLog(null);
    }

    public static JournalEntryLog successLog(ProcessManagerKey createdProcessManagerKey) {
        return new JournalEntryLog(LocalDateTime.now(), JournalEntryLogType.SUCCESS, "Success", createdProcessManagerKey);
    }

    public static JournalEntryLog failureLog(String failureDescription) {
        return new JournalEntryLog(LocalDateTime.now(), JournalEntryLogType.FAILURE, failureDescription, null);
    }

    public static JournalEntryLog ignoreLog() {
        return new JournalEntryLog(LocalDateTime.now(), JournalEntryLogType.IGNORE, "Ignore", null);
    }

    public LocalDateTime getTimestamp() {
        return dateTime;
    }

    private void setDateTime(LocalDateTime dateTime) {
        checkThat(value(dateTime).notNull().because("Log date cannot be null"));
        this.dateTime = dateTime;
    }

    public JournalEntryLogType getType() {
        return type;
    }

    private void setType(JournalEntryLogType type) {
        checkThat(value(type).notNull().because("Log type cannot be null"));
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        checkThat(value(description).notNull().because("Log description cannot be null"));
        this.description = description;
    }

    public ProcessManagerKey getCreatedProcessManagerKey() {
        return createdProcessManagerKey;
    }

    private void setCreatedProcessManagerKey(ProcessManagerKey createdProcessManagerKey) {
        this.createdProcessManagerKey = createdProcessManagerKey;
    }

    public boolean hasCreatedProcessManagerKey() {
        return createdProcessManagerKey != null;
    }

}
