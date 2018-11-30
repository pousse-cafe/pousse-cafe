package poussecafe.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.domain.ComponentFactory;
import poussecafe.domain.ComponentSpecification;
import poussecafe.events.FailedConsumption;
import poussecafe.events.SuccessfulConsumption;
import poussecafe.exception.PousseCafeException;
import poussecafe.messaging.MessageListener;
import poussecafe.messaging.MessageListenerRegistry;
import poussecafe.util.ExceptionUtils;

public class MessageConsumer {

    public synchronized void consumeMessage(RawAndAdaptedMessage message) {
        logger.debug("Handling received message {}", message);
        String consumptionId = UUID.randomUUID().toString();
        Collection<MessageListener> listeners = listenerRegistry.getListeners(message.adapted().getClass());
        List<MessageListener> toRetryInitially = consumeMessage(message, consumptionId, listeners);
        if(!toRetryInitially.isEmpty()) {
            retryConsumption(message, consumptionId, toRetryInitially);
        }
        logger.debug("Message {} handled (consumption ID {})", message, consumptionId);
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
                logger.debug("Optimistic locking failure detected, will retry", e);
                toRetry.add(listener);
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
        logger.debug("Consumption of message {} by listener {}", receivedMessage, listener);
        try {
            listener.consume(receivedMessage.adapted());
            notifySuccessfulConsumption(consumptionId, receivedMessage, listener);
        } catch (Exception e) {
            notifyFailedConsumption(consumptionId, receivedMessage, listener, e);
        }
    }

    private void notifySuccessfulConsumption(String consumptionId,
            RawAndAdaptedMessage receivedMessage,
            MessageListener listener) {
        logger.debug("Consumption of message {} by listener {} succeeded", receivedMessage, listener);
        if(!SuccessfulConsumption.class.isAssignableFrom(receivedMessage.getClass())) {
            try {
                SuccessfulConsumption event = componentFactory.newComponent(ComponentSpecification.ofClass(SuccessfulConsumption.class));
                event.consumptionId().set(consumptionId);
                event.listenerId().set(listener.getListenerId());
                event.rawMessage().set(rawMessageOrDefault(receivedMessage));
                messageSenderLocator.locate(SuccessfulConsumption.class).sendMessage(event);
            } catch (PousseCafeException e) {
                logger.debug("Unable to notify successful consumption", e);
            }
        }
    }

    private ComponentFactory componentFactory;

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
        logger.error("Consumption of message {} by listener {} failed", receivedMessage, listener, e);
        if(!FailedConsumption.class.isAssignableFrom(receivedMessage.getClass())) {
            try {
                FailedConsumption event = componentFactory.newComponent(ComponentSpecification.ofClass(FailedConsumption.class));
                event.consumptionId().set(consumptionId);
                event.listenerId().set(listener.getListenerId());
                event.rawMessage().set(rawMessageOrDefault(receivedMessage));
                event.error().set(ExceptionUtils.getStackTrace(e));
                messageSenderLocator.locate(FailedConsumption.class).sendMessage(event);
            } catch (PousseCafeException e1) {
                logger.error("Unable to notify failed consumption for message {}", rawMessageOrDefault(receivedMessage), e);
            }
        }
    }
}