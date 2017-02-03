package poussecafe.aspect;

import org.slf4j.LoggerFactory;
import poussecafe.configuration.ConsequenceListenerEntry;
import poussecafe.consequence.ConsequenceListenerRegistry;

public aspect ConsequenceListenerRegistrationLogging {

    pointcut onListenerRegistration(ConsequenceListenerEntry entry) :
        call(void ConsequenceListenerRegistry.registerListener(ConsequenceListenerEntry))
        && args(entry);

    before(ConsequenceListenerEntry entry) : onListenerRegistration(entry) {
        LoggerFactory.getLogger(thisJoinPoint.getTarget().getClass()).info("Registring listener " + entry);
    }
}
