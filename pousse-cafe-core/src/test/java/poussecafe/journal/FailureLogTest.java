package poussecafe.journal;

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
