package poussecafe.journal;

import poussecafe.journal.domain.JournalEntryLog;
import poussecafe.journal.domain.JournalEntryLogType;

public class FailureLogTest extends JournalEntryLogTest {

    private String failureDescription;

    @Override
    protected JournalEntryLog buildLog() {
        failureDescription = "description";
        return JournalEntryLog.failureLog(failureDescription);
    }

    @Override
    protected JournalEntryLogType expectedType() {
        return JournalEntryLogType.FAILURE;
    }

    @Override
    protected String expectedDescription() {
        return failureDescription;
    }

}
