package poussecafe.aspect;

import org.slf4j.LoggerFactory;
import poussecafe.messaging.MessageListener;
import poussecafe.messaging.MessageReceiver;
import poussecafe.messaging.Message;

public aspect ConsequenceConsumptionLogging {

    pointcut onConsequenceConsumption(Message aConsequence, MessageListener aListener) :
        call(void MessageReceiver.consumeConsequence(Message, MessageListener))
        && args(aConsequence, aListener);

    before(Message aConsequence, MessageListener aListener) : onConsequenceConsumption(aConsequence, aListener) {
        LoggerFactory.getLogger(thisJoinPoint.getTarget().getClass()).debug("Consumption of consequence " + aConsequence + " by listener " + aListener);
    }
    
    after(Message aConsequence, MessageListener aListener) : onConsequenceConsumption(aConsequence, aListener) {
        LoggerFactory.getLogger(thisJoinPoint.getTarget().getClass()).debug("Consequence " + aConsequence + " consumed by listener " + aListener);
    }
}
