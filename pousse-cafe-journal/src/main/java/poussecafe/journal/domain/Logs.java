package poussecafe.journal.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static java.util.Collections.unmodifiableList;

public class Logs {

    private List<JournalEntryLog> logs = new ArrayList<>();

    public Logs(List<JournalEntryLog> logs) {
        this.logs = logs;
    }

    public JournalEntryLog getSuccessLog() {
        return logs.stream().filter(log -> log.getType() == JournalEntryLogType.SUCCESS).findFirst().orElse(null);
    }

    public JournalEntryLog getLastFailureLog() {
        ListIterator<JournalEntryLog> iterator = logs.listIterator(logs.size());
        while (iterator.hasPrevious()) {
            JournalEntryLog log = iterator.previous();
            if (log.getType() == JournalEntryLogType.FAILURE) {
                return log;
            }
        }
        return null;
    }

    public Logs withAdditional(JournalEntryLog log) {
        List<JournalEntryLog> newLogs = new ArrayList<>(logs);
        newLogs.add(log);
        return new Logs(newLogs);
    }

    public int size() {
        return logs.size();
    }

    public List<JournalEntryLog> asList() {
        return unmodifiableList(logs);
    }
}
