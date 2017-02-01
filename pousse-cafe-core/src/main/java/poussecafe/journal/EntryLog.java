package poussecafe.journal;

import java.time.LocalDateTime;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class EntryLog {

    private LocalDateTime dateTime;

    private EntryLogType type;

    private String description;

    private EntryLog(LocalDateTime dateTime, EntryLogType type, String description) {
        setDateTime(dateTime);
        setType(type);
        setDescription(description);
    }

    public static EntryLog successLog() {
        return new EntryLog(LocalDateTime.now(), EntryLogType.SUCCESS, "Success");
    }

    public static EntryLog failureLog(String failureDescription) {
        return new EntryLog(LocalDateTime.now(), EntryLogType.FAILURE, failureDescription);
    }

    public static EntryLog ignoreLog() {
        return new EntryLog(LocalDateTime.now(), EntryLogType.IGNORE, "Ignore");
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

}
