package poussecafe.journal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import poussecafe.consequence.CommandHandlingResult;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class CommandWatcher {

    private PollingPeriod pollingPeriod;

    private ConsequenceJournal consequenceJournal;

    private List<PollingRequest> requests;

    private Timer timer;

    private CommandWatcher(PollingPeriod pollingPeriod) {
        setPollingPeriod(pollingPeriod);
        requests = new ArrayList<>();
    }

    private void setPollingPeriod(PollingPeriod pollingPeriod) {
        checkThat(value(pollingPeriod).notNull().because("Polling period must be valid"));
        this.pollingPeriod = pollingPeriod;
    }

    public static CommandWatcher withDefaultPollingPeriod() {
        return new CommandWatcher(PollingPeriod.DEFAULT_POLLING_PERIOD);
    }

    public static CommandWatcher withPollingPeriod(PollingPeriod period) {
        return new CommandWatcher(period);
    }

    public void startWatching() {
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handlePendingRequests();
            }
        }, 0, pollingPeriod.toMillis());
    }

    public void stopWatching() {
        timer.cancel();
    }

    private synchronized void handlePendingRequests() {
        Iterator<PollingRequest> iterator = requests.iterator();
        while (iterator.hasNext()) {
            PollingRequest request = iterator.next();
            Entry journalEntry = consequenceJournal.findCommandEntry(request.getConsequenceId());
            if (journalEntry != null) {
                if (journalEntry.hasLogWithType(EntryLogType.SUCCESS)) {
                    request.completeWithSuccess();
                    iterator.remove();
                } else if (journalEntry.hasLogWithType(EntryLogType.FAILURE)) {
                    request.completeWithFailure(journalEntry.getLastFailureLog().getDescription());
                    iterator.remove();
                }
            }
        }
    }

    public synchronized Future<CommandHandlingResult> watchCommand(String commandId) {
        CompletableFuture<CommandHandlingResult> task = new CompletableFuture<>();
        requests.add(new PollingRequest(commandId, task));
        return task;
    }

    public void setConsequenceJournal(ConsequenceJournal consequenceJournal) {
        checkThat(value(consequenceJournal).notNull().because("Consequence journal cannot be null"));
        this.consequenceJournal = consequenceJournal;
    }

}
