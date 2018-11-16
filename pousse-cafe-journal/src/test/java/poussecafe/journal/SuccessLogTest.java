package poussecafe.journal;

import poussecafe.journal.domain.JournalEntryLog;
import poussecafe.journal.domain.JournalEntryLogType;

public class SuccessLogTest extends JournalEntryLogTest {

    @Override
    protected JournalEntryLog buildLog() {
        return JournalEntryLog.successLog();
    }

    @Override
    protected JournalEntryLogType expectedType() {
        return JournalEntryLogType.SUCCESS;
    }

    @Override
    protected String expectedDescription() {
        return "Success";
    }

}
