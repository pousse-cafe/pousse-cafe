package poussecafe.aspect;

import org.slf4j.LoggerFactory;
import poussecafe.messaging.MessageSender;
import poussecafe.messaging.Message;

public aspect ConsequenceEmissionLogging {

    pointcut onConsequenceEmission(Message aConsequence) :
        call(void MessageSender.emitConsequence(Message))
        && args(aConsequence);

    before(Message aConsequence) : onConsequenceEmission(aConsequence) {
        MessageSender emitter = (MessageSender) thisJoinPoint.getTarget();
        LoggerFactory.getLogger(emitter.getClass()).info("Emitting consequence " + aConsequence + " on source " + emitter.getDestinationQueue());
    }
}
