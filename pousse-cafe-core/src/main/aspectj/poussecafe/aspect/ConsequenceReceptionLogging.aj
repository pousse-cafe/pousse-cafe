package poussecafe.aspect;

import org.slf4j.LoggerFactory;
import poussecafe.consequence.Consequence;
import poussecafe.consequence.ConsequenceReceiver;

public aspect ConsequenceReceptionLogging {

    pointcut onConsequenceReception(Consequence aConsequence) :
        call(void ConsequenceReceiver.onConsequence(Consequence))
        && args(aConsequence);

    before(Consequence aConsequence) : onConsequenceReception(aConsequence) {
        LoggerFactory.getLogger(thisJoinPoint.getTarget().getClass()).info("Handling received consequence " + aConsequence);
    }

    after(Consequence aConsequence) : onConsequenceReception(aConsequence) {
        LoggerFactory.getLogger(thisJoinPoint.getTarget().getClass()).info("Consequence " + aConsequence + " handled");
    }
}
