package poussecafe.journal;

import org.junit.Test;
import poussecafe.consequence.Consequence;
import poussecafe.data.memory.InMemoryDataFactory;
import poussecafe.domain.DomainException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JournalEntryTest {

    private Consequence consequence;

    private JournalEntry entry;

    @Test
    public void loggingSuccessAddsLog() {
        givenEntry();
        whenLoggingSuccess();
        thenSuccessLogAdded();
    }

    private void givenEntry() {
        givenConsequence();
        JournalEntryFactory entryFactory = new JournalEntryFactory();
        entryFactory.setStorableDataFactory(new InMemoryDataFactory<>(JournalEntry.Data.class));
        entry = entryFactory.buildEntryForEmittedConsequence(new JournalEntryKey(consequence.getId(), "listenerId"),
                consequence);
    }

    private void givenConsequence() {
        consequence = mock(Consequence.class);
        when(consequence.getId()).thenReturn("consequenceId");
    }

    private void whenLoggingSuccess() {
        entry.logSuccess();
    }

    private void thenSuccessLogAdded() {
        assertThat(entry.getLogs().get(entry.getLogs().size() - 1).getType(), equalTo(JournalEntryLogType.SUCCESS));
    }

    @Test
    public void loggedSucessIsDetected() {
        givenEntryWithSuccessLogged();
        thenSuccessLogDetected();
    }

    private void givenEntryWithSuccessLogged() {
        givenEntry();
        entry.logSuccess();
    }

    private void thenSuccessLogDetected() {
        assertTrue(entry.getStatus() == JournalEntryStatus.SUCCESS);
    }

    @Test
    public void loggingIgnoredAddsLog() {
        givenEntry();
        whenLoggingIgnored();
        thenIgnoredLogAdded();
    }

    private void whenLoggingIgnored() {
        entry.logIgnored();
    }

    private void thenIgnoredLogAdded() {
        assertThat(entry.getLogs().get(entry.getLogs().size() - 1).getType(), equalTo(JournalEntryLogType.IGNORE));
    }

    @Test
    public void loggingFailureAddsLog() {
        givenEntry();
        whenLoggingFailure();
        thenFailureLogAdded();
    }

    private void whenLoggingFailure() {
        entry.logFailure("description");
    }

    private void thenFailureLogAdded() {
        assertThat(entry.getLogs().get(entry.getLogs().size() - 1).getType(), equalTo(JournalEntryLogType.FAILURE));
    }

    @Test(expected = DomainException.class)
    public void cannotLogSuccessMoreThanOnce() {
        givenEntry();
        whenLoggingSuccessTwice();
    }

    private void whenLoggingSuccessTwice() {
        entry.logSuccess();
        entry.logSuccess();
    }

    @Test(expected = DomainException.class)
    public void cannotLogFailureIfAlreadySuccessful() {
        givenEntryWithSuccessLogged();
        whenLoggingFailure();
    }

    @Test
    public void canLogIgnoredIfAlreadySuccessful() {
        givenEntryWithSuccessLogged();
        whenLoggingIgnored();
        thenIgnoredLogAdded();
    }
}
