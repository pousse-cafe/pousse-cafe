package poussecafe.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.environment.Environment;
import poussecafe.environment.MessageListener;
import poussecafe.exception.PousseCafeException;
import poussecafe.exception.SameOperationException;
import poussecafe.support.model.FailedConsumption;
import poussecafe.support.model.SuccessfulConsumption;
import poussecafe.util.ExceptionUtils;

import static java.util.stream.Collectors.toList;

public class MessageConsumer {

    public static class Builder {

        private MessageConsumer messageConsumer = new MessageConsumer();

        public Builder environment(Environment environment) {
            messageConsumer.environment = environment;
            return this;
        }

        public Builder messageSenderLocator(MessageSenderLocator messageSenderLocator) {
            messageConsumer.messageSenderLocator = messageSenderLocator;
            return this;
        }

        public Builder failFast(boolean failFast) {
            messageConsumer.failFast = failFast;
            return this;
        }

        public MessageConsumer build() {
            Objects.requireNonNull(messageConsumer.environment);
            Objects.requireNonNull(messageConsumer.messageSenderLocator);
            return messageConsumer;
        }
    }

    private MessageConsumer() {

    }

    private boolean failFast;

    private boolean consumptionFailure;

    public synchronized void consumeMessage(OriginalAndMarshaledMessage message) {
        if(consumptionInterrupted()) {
            throw new FailFastException();
        }

        logger.debug("Handling received message {}", message.original());
        String consumptionId = UUID.randomUUID().toString();
        List<MessageListener> listeners = environment.messageListenersOf(message.original().getClass()).stream()
                .sorted()
                .collect(toList());
        logger.debug("Found {} listeners", listeners.size());
        List<MessageListener> toRetryInitially = consumeMessage(message, consumptionId, listeners);
        if(!toRetryInitially.isEmpty()) {
            retryConsumption(message, consumptionId, toRetryInitially);
        }
        logger.debug("Message {} handled (consumption ID {})", message.original(), consumptionId);
    }

    private boolean consumptionInterrupted() {
        return failFast && consumptionFailure;
    }

    private Environment environment;

    private List<MessageListener> consumeMessage(OriginalAndMarshaledMessage message,
            String consumptionId,
            List<MessageListener> listeners) {
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

    private void retryConsumption(OriginalAndMarshaledMessage message,
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
            OriginalAndMarshaledMessage receivedMessage,
            MessageListener listener) {
        logger.debug("Consumption of message {} by listener {}", receivedMessage.original(), listener);
        try {
            listener.consumer().accept(receivedMessage.original());
            notifySuccessfulConsumption(consumptionId, receivedMessage, listener);
        } catch (Exception e) {
            if(failFast) {
                logger.error("Failing fast on exception from listener {}", listener, e);
                consumptionFailure = true;
                throw new FailFastException();
            } else {
                notifyFailedConsumption(consumptionId, receivedMessage, listener, e);
            }
        }
    }

    private void notifySuccessfulConsumption(String consumptionId,
            OriginalAndMarshaledMessage receivedMessage,
            MessageListener listener) {
        logger.debug("Consumption of message {} by listener {} succeeded", receivedMessage.original(), listener);
        if(!SuccessfulConsumption.class.isAssignableFrom(receivedMessage.getClass())) {
            try {
                SuccessfulConsumption event = environment.messageFactory().newMessage(SuccessfulConsumption.class);
                event.consumptionId().value(consumptionId);
                event.listenerId().value(listener.id());
                event.rawMessage().value(marshaledMessageOrDefault(receivedMessage));
                messageSenderLocator.locate(SuccessfulConsumption.class).sendMessage(event);
            } catch (PousseCafeException e) {
                logger.debug("Unable to notify successful consumption", e);
            }
        }
    }

    private String marshaledMessageOrDefault(OriginalAndMarshaledMessage receivedMessage) {
        if(receivedMessage.marshaled() instanceof String) {
            return (String) receivedMessage.marshaled();
        } else {
            return receivedMessage.original().toString();
        }
    }

    private MessageSenderLocator messageSenderLocator;

    private void notifyFailedConsumption(String consumptionId,
            OriginalAndMarshaledMessage receivedMessage,
            MessageListener listener,
            Exception e) {
        logger.error("Consumption of message {} by listener {} failed", receivedMessage.original(), listener, e);
        if(!FailedConsumption.class.isAssignableFrom(receivedMessage.getClass())) {
            try {
                FailedConsumption event = environment.messageFactory().newMessage(FailedConsumption.class);
                event.consumptionId().value(consumptionId);
                event.listenerId().value(listener.id());
                event.rawMessage().value(marshaledMessageOrDefault(receivedMessage));
                event.error().value(ExceptionUtils.getStackTrace(e));
                messageSenderLocator.locate(FailedConsumption.class).sendMessage(event);
            } catch (PousseCafeException e1) {
                logger.error("Unable to notify failed consumption for message {}", marshaledMessageOrDefault(receivedMessage), e1);
            }
        }
    }
}
