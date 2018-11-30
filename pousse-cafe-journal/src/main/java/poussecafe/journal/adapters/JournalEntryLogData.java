package poussecafe.journal.adapters;

import java.io.Serializable;
import java.time.LocalDateTime;
import poussecafe.journal.domain.JournalEntryLog;
import poussecafe.journal.domain.JournalEntryLogType;

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
