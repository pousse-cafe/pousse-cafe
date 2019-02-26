package poussecafe.runtime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.environment.MessageFactory;
import poussecafe.exception.PousseCafeException;
import poussecafe.exception.SameOperationException;
import poussecafe.messaging.MessageListener;
import poussecafe.messaging.MessageListenerRegistry;
import poussecafe.support.model.FailedConsumption;
import poussecafe.support.model.SuccessfulConsumption;
import poussecafe.util.ExceptionUtils;

public class MessageConsumer {

    public synchronized void consumeMessage(RawAndAdaptedMessage message) {
        logger.debug("Handling received message {}", message.adapted());
        String consumptionId = UUID.randomUUID().toString();
        Collection<MessageListener> listeners = listenerRegistry.getListeners(message.adapted().getClass());
        List<MessageListener> toRetryInitially = consumeMessage(message, consumptionId, listeners);
        if(!toRetryInitially.isEmpty()) {
            retryConsumption(message, consumptionId, toRetryInitially);
        }
        logger.debug("Message {} handled (consumption ID {})", message.adapted(), consumptionId);
    }

    private MessageListenerRegistry listenerRegistry;

    private List<MessageListener> consumeMessage(RawAndAdaptedMessage message,
            String consumptionId,
            Collection<MessageListener> listeners) {
        List<MessageListener> toRetry = new ArrayList<>();
        for (MessageListener listener : listeners) {
            try {
                consumeMessage(consumptionId, message, listener);
            } catch (OptimisticLockingException e) {
                logger.warn("Optimistic locking failure detected, will retry", e);
                toRetry.add(listener);
            } catch (SameOperationException e) {
                logger.warn("Ignoring probable dubbed message consumption", e);
            }
        }
        return toRetry;
    }

    private void retryConsumption(RawAndAdaptedMessage message,
            String consumptionId,
            List<MessageListener> toRetryInitially) {
        int retry = 1;
        List<MessageListener> toRetry = toRetryInitially;
        while(!toRetry.isEmpty() && retry <= MAX_RETRIES) {
            logger.debug("Try #{} for {} listeners", retry, toRetry.size());
            toRetry = consumeMessage(message, consumptionId, toRetry);
            ++retry;
        }
    }

    private static final int MAX_RETRIES = 10;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private void consumeMessage(String consumptionId,
            RawAndAdaptedMessage receivedMessage,
            MessageListener listener) {
        logger.debug("Consumption of message {} by listener {}", receivedMessage.adapted(), listener);
        try {
            listener.consumer().accept(receivedMessage.adapted());
            notifySuccessfulConsumption(consumptionId, receivedMessage, listener);
        } catch (Exception e) {
            notifyFailedConsumption(consumptionId, receivedMessage, listener, e);
        }
    }

    private void notifySuccessfulConsumption(String consumptionId,
            RawAndAdaptedMessage receivedMessage,
            MessageListener listener) {
        logger.debug("Consumption of message {} by listener {} succeeded", receivedMessage.adapted(), listener);
        if(!SuccessfulConsumption.class.isAssignableFrom(receivedMessage.getClass())) {
            try {
                SuccessfulConsumption event = messageFactory.newMessage(SuccessfulConsumption.class);
                event.consumptionId().value(consumptionId);
                event.listenerId().value(listener.id());
                event.rawMessage().value(rawMessageOrDefault(receivedMessage));
                messageSenderLocator.locate(SuccessfulConsumption.class).sendMessage(event);
            } catch (PousseCafeException e) {
                logger.debug("Unable to notify successful consumption", e);
            }
        }
    }

    private MessageFactory messageFactory;

    private String rawMessageOrDefault(RawAndAdaptedMessage receivedMessage) {
        if(receivedMessage.raw() instanceof String) {
            return (String) receivedMessage.raw();
        } else {
            return receivedMessage.adapted().toString();
        }
    }

    private MessageSenderLocator messageSenderLocator;

    private void notifyFailedConsumption(String consumptionId,
            RawAndAdaptedMessage receivedMessage,
            MessageListener listener,
            Exception e) {
        logger.error("Consumption of message {} by listener {} failed", receivedMessage.adapted(), listener, e);
        if(!FailedConsumption.class.isAssignableFrom(receivedMessage.getClass())) {
            try {
                FailedConsumption event = messageFactory.newMessage(FailedConsumption.class);
                event.consumptionId().value(consumptionId);
                event.listenerId().value(listener.id());
                event.rawMessage().value(rawMessageOrDefault(receivedMessage));
                event.error().value(ExceptionUtils.getStackTrace(e));
                messageSenderLocator.locate(FailedConsumption.class).sendMessage(event);
            } catch (PousseCafeException e1) {
                logger.error("Unable to notify failed consumption for message {}", rawMessageOrDefault(receivedMessage), e1);
            }
        }
    }
}
