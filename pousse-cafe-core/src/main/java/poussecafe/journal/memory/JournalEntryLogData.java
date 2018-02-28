package poussecafe.journal.memory;

import java.io.Serializable;
import java.time.LocalDateTime;
import poussecafe.journal.JournalEntryLog;
import poussecafe.journal.JournalEntryLogType;

@SuppressWarnings("serial")
public class JournalEntryLogData implements Serializable {

    public JournalEntryLogData(JournalEntryLog log) {
        dateTime = log.getTimestamp();
        type = log.getType();
        description = log.getDescription();
    }

    public JournalEntryLog toLog() {
        return new JournalEntryLog(dateTime, type, description);
    }

    public LocalDateTime dateTime;

    public JournalEntryLogType type;

    public String description;
}
