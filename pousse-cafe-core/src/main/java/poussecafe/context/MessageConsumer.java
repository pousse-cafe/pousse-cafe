package poussecafe.context;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.messaging.FailedConsumption;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageListener;
import poussecafe.messaging.MessageListenerRegistry;
import poussecafe.messaging.SuccessfulConsumption;
import poussecafe.util.ExceptionUtils;

public class MessageConsumer {

    public synchronized void consumeMessage(Message message) {
        logger.debug("Handling received message {}", message);
        String consumptionId = UUID.randomUUID().toString();
        for (MessageListener listener : listenerRegistry.getListeners(message.getClass())) {
            consumeMessage(consumptionId, message, listener);
        }
        logger.debug("Message {} handled (consumption ID {})", message, consumptionId);
    }

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private MessageListenerRegistry listenerRegistry;

    private void consumeMessage(String consumptionId,
            Message receivedMessage,
            MessageListener listener) {
        logger.debug("Consumption of message {} by listener {}", receivedMessage, listener);
        try {
            listener.consume(receivedMessage);
            handleLocalMessage(new SuccessfulConsumption(consumptionId, listener.getListenerId(), receivedMessage));
        } catch (Exception e) {
            logger.warn("Consumption of message {} by listener {} failed", receivedMessage, listener, e);
            handleLocalMessage(new FailedConsumption(consumptionId, listener.getListenerId(), receivedMessage, ExceptionUtils.getStackTrace(e)));
        }
        logger.debug("Message {} consumed by listener {}", receivedMessage, listener);
    }

    private void handleLocalMessage(Message message) {
        for (MessageListener listener : listenerRegistry.getListeners(message.getClass())) {
            consumeLocalMessage(message, listener);
        }
    }

    private void consumeLocalMessage(Message receivedMessage,
            MessageListener listener) {
        logger.debug("Consumption of local message {} by listener {}", receivedMessage, listener);
        try {
            listener.consume(receivedMessage);
        } catch (Exception e) {
            logger.error("Unable to consume local message", e);
        }
        logger.debug("Local message {} consumed by listener {}", receivedMessage, listener);
    }
}
