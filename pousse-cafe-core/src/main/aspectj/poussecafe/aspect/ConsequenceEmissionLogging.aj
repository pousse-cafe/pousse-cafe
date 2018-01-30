package poussecafe.aspect;

import org.slf4j.LoggerFactory;
import poussecafe.messaging.MessageSender;
import poussecafe.messaging.Message;

public aspect ConsequenceEmissionLogging {

    pointcut onMessageSending(Message aMessage) :
        call(void MessageSender.sendMessage(Message))
        && args(aMessage);

    before(Message aMessage) : onMessageSending(aMessage) {
        MessageSender emitter = (MessageSender) thisJoinPoint.getTarget();
        LoggerFactory.getLogger(emitter.getClass()).info("Sending message " + aMessage);
    }
}
