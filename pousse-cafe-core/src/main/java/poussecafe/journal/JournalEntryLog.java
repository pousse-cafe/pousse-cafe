package poussecafe.journal;

import java.time.LocalDateTime;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class JournalEntryLog {

    private LocalDateTime dateTime;

    private JournalEntryLogType type;

    private String description;

    public JournalEntryLog(LocalDateTime dateTime, JournalEntryLogType type, String description) {
        setDateTime(dateTime);
        setType(type);
        setDescription(description);
    }

    public static JournalEntryLog successLog() {
        return new JournalEntryLog(LocalDateTime.now(), JournalEntryLogType.SUCCESS, "Success");
    }

    public static JournalEntryLog failureLog(String failureDescription) {
        return new JournalEntryLog(LocalDateTime.now(), JournalEntryLogType.FAILURE, failureDescription);
    }

    public static JournalEntryLog ignoreLog() {
        return new JournalEntryLog(LocalDateTime.now(), JournalEntryLogType.IGNORE, "Ignore");
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

}
