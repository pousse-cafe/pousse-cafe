package poussecafe.journal;

import org.junit.Test;
import poussecafe.journal.domain.JournalEntryLog;
import poussecafe.journal.domain.JournalEntryLogType;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public abstract class JournalEntryLogTest {

    private JournalEntryLog entryLog;

    @Test
    public void logHasTimestamp() {
        whenBuildingLog();
        thenLogHasTimestamp();
    }

    protected void whenBuildingLog() {
        entryLog = buildLog();
    }

    protected abstract JournalEntryLog buildLog();

    private void thenLogHasTimestamp() {
        assertThat(entryLog.getTimestamp(), notNullValue());
    }

    @Test
    public void logHasExpectedType() {
        whenBuildingLog();
        thenLogHasType(expectedType());
    }

    protected abstract JournalEntryLogType expectedType();

    private void thenLogHasType(JournalEntryLogType expectedType) {
        assertThat(entryLog.getType(), is(expectedType));
    }

    @Test
    public void logHasExpectedDescription() {
        whenBuildingLog();
        thenLogHasDescription(expectedDescription());
    }

    protected abstract String expectedDescription();

    private void thenLogHasDescription(String expectedDescription) {
        assertThat(entryLog.getDescription(), is(expectedDescription));
    }
}
