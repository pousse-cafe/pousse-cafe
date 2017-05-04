package poussecafe.aspect;

import org.slf4j.LoggerFactory;
import poussecafe.messaging.MessageReceiver;
import poussecafe.messaging.Message;

public aspect ConsequenceReceptionLogging {

    pointcut onConsequenceReception(Message aConsequence) :
        call(void MessageReceiver.onConsequence(Message))
        && args(aConsequence);

    before(Message aConsequence) : onConsequenceReception(aConsequence) {
        LoggerFactory.getLogger(thisJoinPoint.getTarget().getClass()).info("Handling received consequence " + aConsequence);
    }

    after(Message aConsequence) : onConsequenceReception(aConsequence) {
        LoggerFactory.getLogger(thisJoinPoint.getTarget().getClass()).info("Consequence " + aConsequence + " handled");
    }
}
