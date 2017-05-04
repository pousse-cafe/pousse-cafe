package poussecafe.aspect;

import org.slf4j.LoggerFactory;
import poussecafe.journal.MessagingJournal;
import poussecafe.messaging.Message;

public aspect FailedConsumptionLogging {

    pointcut onConsequenceConsumptionFailed(String listenerId, Message consequence, Exception exception) :
        call(void MessagingJournal.logFailedConsumption(String, Message, Exception))
        && args(listenerId, consequence, exception);

    before(String listenerId, Message consequence, Exception exception) : onConsequenceConsumptionFailed(listenerId, consequence, exception) {
        LoggerFactory.getLogger(thisJoinPoint.getTarget().getClass()).error("Consumption of consequence " + consequence
                + " by listener " + listenerId + " failed", exception);
    }
}
