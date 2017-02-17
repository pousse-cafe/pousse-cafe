package poussecafe.journal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import poussecafe.consequence.CommandHandlingResult;
import poussecafe.process.ProcessManager;
import poussecafe.process.ProcessManagerRepository;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class CommandWatcher {

    private PollingPeriod pollingPeriod;

    private ConsequenceJournal consequenceJournal;

    private List<PollingRequest> requests;

    private Timer timer;

    private ProcessManagerRepository processManagerRepository;

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
            if (request.hasProcessManagerKey()) {
                handlePendingRequestWithProcessManager(iterator, request);
            } else {
                handlePendingRequest(iterator, request);
            }
        }
    }

    private void handlePendingRequestWithProcessManager(Iterator<PollingRequest> iterator,
            PollingRequest request) {
        ProcessManager processManager = processManagerRepository.get(request.getProcessManagerKey());
        if (processManager.isInFinalState()) {
            request.completeWithSuccess();
            iterator.remove();
        } else if (processManager.isInErrorState()) {
            request.completeWithFailure(processManager.getErrorState().getErrorDescription());
            iterator.remove();
        }
    }

    private void handlePendingRequest(Iterator<PollingRequest> iterator,
            PollingRequest request) {
        JournalEntry journalEntry = consequenceJournal.findCommandEntry(request.getConsequenceId());
        if (journalEntry != null) {
            if (journalEntry.getStatus() == JournalEntryStatus.SUCCESS) {
                handlePendingRequestIfSuccessfulHandling(iterator, request, journalEntry.getSuccessLog());
            } else if (journalEntry.getStatus() == JournalEntryStatus.FAILURE) {
                handlePendingRequestIfFailedHandling(iterator, request, journalEntry.getLastFailureLog());
            }
        }
    }

    private void handlePendingRequestIfSuccessfulHandling(Iterator<PollingRequest> iterator,
            PollingRequest request,
            JournalEntryLog log) {
        if(!log.hasCreatedProcessManagerKey()) {
            request.completeWithSuccess();
            iterator.remove();
        } else {
            request.setProcessManagerKey(log.getCreatedProcessManagerKey());
            handlePendingRequestWithProcessManager(iterator, request);
        }
    }

    private void handlePendingRequestIfFailedHandling(Iterator<PollingRequest> iterator,
            PollingRequest request,
            JournalEntryLog log) {
        request.completeWithFailure(log.getDescription());
        iterator.remove();
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

    public void setProcessManagerRepository(ProcessManagerRepository processManagerRepository) {
        checkThat(value(processManagerRepository).notNull().because("Process manager repository cannot be null"));
        this.processManagerRepository = processManagerRepository;
    }

}
