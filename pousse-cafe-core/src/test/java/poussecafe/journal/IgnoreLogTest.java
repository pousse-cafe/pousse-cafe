package poussecafe.journal;

public class IgnoreLogTest extends EntryLogTest {

    @Override
    protected EntryLog buildLog() {
        return EntryLog.ignoreLog();
    }

    @Override
    protected EntryLogType expectedType() {
        return EntryLogType.IGNORE;
    }

    @Override
    protected String expectedDescription() {
        return "Ignore";
    }

}
