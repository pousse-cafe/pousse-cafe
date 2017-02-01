package poussecafe.journal;

public class SuccessLogTest extends EntryLogTest {

    @Override
    protected EntryLog buildLog() {
        return EntryLog.successLog();
    }

    @Override
    protected EntryLogType expectedType() {
        return EntryLogType.SUCCESS;
    }

    @Override
    protected String expectedDescription() {
        return "Success";
    }

}
