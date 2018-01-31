package poussecafe.aspect;

import org.slf4j.LoggerFactory;
import poussecafe.context.MessageListenerEntry;
import poussecafe.messaging.MessageListenerRegistry;

public aspect ConsequenceListenerRegistrationLogging {

    pointcut onListenerRegistration(MessageListenerEntry entry) :
        call(void MessageListenerRegistry.registerListener(MessageListenerEntry))
        && args(entry);

    before(MessageListenerEntry entry) : onListenerRegistration(entry) {
        LoggerFactory.getLogger(thisJoinPoint.getTarget().getClass()).info("Registring listener " + entry);
    }
}
