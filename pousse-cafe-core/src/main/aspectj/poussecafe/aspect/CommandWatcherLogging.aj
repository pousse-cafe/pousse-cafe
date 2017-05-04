package poussecafe.aspect;

import java.util.concurrent.Future;
import org.slf4j.LoggerFactory;
import poussecafe.journal.CommandWatcher;
import poussecafe.messaging.CommandHandlingResult;

public aspect CommandWatcherLogging {
    
    pointcut pollingStarted() :
        call(void CommandWatcher.startWatching());

    pointcut onJournalPolling() :
        call(void CommandWatcher.handlePendingRequests());
    
    pointcut requestSubmitted(String commandId) :
        call(Future<CommandHandlingResult> CommandWatcher.watchCommand(String)) && args(commandId);
    
    before() : pollingStarted() {
        LoggerFactory.getLogger(thisJoinPoint.getTarget().getClass()).info("Start handling command watching requests");
    }

    before() : onJournalPolling() {
        LoggerFactory.getLogger(thisJoinPoint.getTarget().getClass()).debug("Handling command watch requests");
    }
    
    before(String commandId) : requestSubmitted(commandId) {
        LoggerFactory.getLogger(thisJoinPoint.getTarget().getClass()).debug("Received request to watch command with ID " + commandId);
    }
}
