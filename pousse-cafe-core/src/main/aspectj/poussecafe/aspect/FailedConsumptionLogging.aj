package poussecafe.aspect;

import org.slf4j.LoggerFactory;
import poussecafe.consequence.Consequence;
import poussecafe.journal.ConsequenceJournal;

public aspect FailedConsumptionLogging {

    pointcut onConsequenceConsumptionFailed(String listenerId, Consequence consequence, Exception exception) :
        call(void ConsequenceJournal.logFailedConsumption(String, Consequence, Exception))
        && args(listenerId, consequence, exception);

    before(String listenerId, Consequence consequence, Exception exception) : onConsequenceConsumptionFailed(listenerId, consequence, exception) {
        LoggerFactory.getLogger(thisJoinPoint.getTarget().getClass()).error("Consumption of consequence " + consequence
                + " by listener " + listenerId + " failed", exception);
    }
}
