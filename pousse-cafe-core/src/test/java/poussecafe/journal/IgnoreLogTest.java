package poussecafe.journal;

public class IgnoreLogTest extends JournalEntryLogTest {

    @Override
    protected JournalEntryLog buildLog() {
        return JournalEntryLog.ignoreLog();
    }

    @Override
    protected JournalEntryLogType expectedType() {
        return JournalEntryLogType.IGNORE;
    }

    @Override
    protected String expectedDescription() {
        return "Ignore";
    }

}
