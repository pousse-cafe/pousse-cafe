package poussecafe.aspect;

import org.slf4j.LoggerFactory;
import poussecafe.consequence.Consequence;
import poussecafe.consequence.ConsequenceListener;
import poussecafe.consequence.ConsequenceReceiver;

public aspect ConsequenceConsumptionLogging {

    pointcut onConsequenceConsumption(Consequence aConsequence, ConsequenceListener aListener) :
        call(void ConsequenceReceiver.consumeConsequence(Consequence, ConsequenceListener))
        && args(aConsequence, aListener);

    before(Consequence aConsequence, ConsequenceListener aListener) : onConsequenceConsumption(aConsequence, aListener) {
        LoggerFactory.getLogger(thisJoinPoint.getTarget().getClass()).debug("Consumption of consequence " + aConsequence + " by listener " + aListener);
    }
    
    after(Consequence aConsequence, ConsequenceListener aListener) : onConsequenceConsumption(aConsequence, aListener) {
        LoggerFactory.getLogger(thisJoinPoint.getTarget().getClass()).debug("Consequence " + aConsequence + " consumed by listener " + aListener);
    }
}
