package poussecafe.journal;

import java.time.LocalDateTime;
import poussecafe.process.ProcessManagerKey;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class EntryLog {

    private LocalDateTime dateTime;

    private EntryLogType type;

    private String description;

    private ProcessManagerKey createdProcessManagerKey;

    private EntryLog(LocalDateTime dateTime, EntryLogType type, String description,
            ProcessManagerKey createdProcessManagerKey) {
        setDateTime(dateTime);
        setType(type);
        setDescription(description);
        setCreatedProcessManagerKey(createdProcessManagerKey);
    }

    public static EntryLog successLog() {
        return successLog(null);
    }

    public static EntryLog successLog(ProcessManagerKey createdProcessManagerKey) {
        return new EntryLog(LocalDateTime.now(), EntryLogType.SUCCESS, "Success", createdProcessManagerKey);
    }

    public static EntryLog failureLog(String failureDescription) {
        return new EntryLog(LocalDateTime.now(), EntryLogType.FAILURE, failureDescription, null);
    }

    public static EntryLog ignoreLog() {
        return new EntryLog(LocalDateTime.now(), EntryLogType.IGNORE, "Ignore", null);
    }

    public LocalDateTime getTimestamp() {
        return dateTime;
    }

    private void setDateTime(LocalDateTime dateTime) {
        checkThat(value(dateTime).notNull().because("Log date cannot be null"));
        this.dateTime = dateTime;
    }

    public EntryLogType getType() {
        return type;
    }

    private void setType(EntryLogType type) {
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
