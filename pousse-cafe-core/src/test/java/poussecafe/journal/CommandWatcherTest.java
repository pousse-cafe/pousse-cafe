package poussecafe.journal;

import java.time.Duration;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import poussecafe.configuration.TestCommand;
import poussecafe.messaging.CommandHandlingResult;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class CommandWatcherTest {

    private MessagingJournal messagingJournal;

    private CommandWatcher watcher;

    private long waitTimeBeforeAtLeastOnePoll;

    private TestCommand command;

    private Future<CommandHandlingResult> watchFuture;

    @Test
    public void journalNotQueriesIfNoRequest() {
        givenStartedWatcher();
        whenWaitingForAtLeastPolls(1);
        thenJournalWasNotQueries();
    }

    private void givenStartedWatcher() {
        givenWatcher();
        watcher.startWatching();
    }

    protected void givenWatcher() {
        messagingJournal = mock(MessagingJournal.class);
        waitTimeBeforeAtLeastOnePoll = 10;
        watcher = CommandWatcher
                .withPollingPeriod(PollingPeriod.withPeriod(Duration.ofMillis(waitTimeBeforeAtLeastOnePoll)));
        watcher.setMessagingJournal(messagingJournal);
    }

    private void whenWaitingForAtLeastPolls(int polls) {
        waitForAtLeastPolls(polls);
    }

    private void waitForAtLeastPolls(int polls) {
        try {
            Thread.sleep(waitTimeBeforeAtLeastOnePoll * polls + 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        watcher.stopWatching();
    }

    private void thenJournalWasNotQueries() {
        verifyZeroInteractions(messagingJournal);
    }

    @Test
    public void journalQueriedIfRequest() {
        givenStartedWatcher();
        givenSubmittedWatchRequest();
        whenWaitingForAtLeastPolls(2);
        thenJournalWasQueriedTwice();
    }

    private void givenSubmittedWatchRequest() {
        command = new TestCommand();
        watchFuture = watcher.watchCommand(command.getId());
    }

    private void thenJournalWasQueriedTwice() {
        verify(messagingJournal, atLeast(2)).findCommandEntry(command.getId());
    }

    @Test
    public void successResultIfSuccessfulHandling() {
        givenWatcher();
        givenSubmittedWatchRequest();
        givenSuccessfulCommandHandling();
        givenWatcherStarted();
        whenWaitingForAtLeastPolls(1);
        thenResultIsSuccess(true);
    }

    private void givenWatcherStarted() {
        watcher.startWatching();
    }

    private void givenSuccessfulCommandHandling() {
        givenEntryWithStatus(JournalEntryStatus.SUCCESS);
    }

    private void givenEntryWithStatus(JournalEntryStatus status) {
        JournalEntry entry = mock(JournalEntry.class);
        when(entry.getStatus()).thenReturn(status);
        when(messagingJournal.findCommandEntry(command.getId())).thenReturn(entry);
        if (status == JournalEntryStatus.FAILURE) {
            JournalEntryLog log = mock(JournalEntryLog.class);
            when(log.getDescription()).thenReturn("description");
            when(entry.getLastFailureLog()).thenReturn(log);
        } else {
            JournalEntryLog log = mock(JournalEntryLog.class);
            when(entry.getSuccessLog()).thenReturn(log);
        }
    }

    private void thenResultIsSuccess(boolean expectedSuccess) {
        try {
            assertThat(watchFuture.get(waitTimeBeforeAtLeastOnePoll + 100, TimeUnit.MILLISECONDS).isSuccess(),
                    is(expectedSuccess));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void failureResultIfFailedHandling() {
        givenWatcher();
        givenSubmittedWatchRequest();
        givenFailedCommandHandling();
        givenWatcherStarted();
        whenWaitingForAtLeastPolls(1);
        thenResultIsSuccess(false);
    }

    private void givenFailedCommandHandling() {
        givenEntryWithStatus(JournalEntryStatus.FAILURE);
    }
}
