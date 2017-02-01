package poussecafe.journal;

public class FailureLogTest extends EntryLogTest {

    private String failureDescription;

    @Override
    protected EntryLog buildLog() {
        failureDescription = "description";
        return EntryLog.failureLog(failureDescription);
    }

    @Override
    protected EntryLogType expectedType() {
        return EntryLogType.FAILURE;
    }

    @Override
    protected String expectedDescription() {
        return failureDescription;
    }

}
