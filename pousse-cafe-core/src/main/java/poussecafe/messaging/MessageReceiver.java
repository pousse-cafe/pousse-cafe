package poussecafe.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.util.ExceptionUtils;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public abstract class MessageReceiver {

    private MessageListenerRegistry listenerRegistry;

    private boolean started;

    protected void onMessage(Message receivedMessage) {
        logger.debug("Handling received message {}", receivedMessage);
        checkThat(value(receivedMessage).notNull().because("Received message cannot be null"));
        for (MessageListener listener : listenerRegistry
                .getListeners(new MessageListenerRoutingKey(receivedMessage.getClass()))) {
            consumeMessage(receivedMessage, listener);
        }
        logger.debug("Message {} handled", receivedMessage);
    }

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private void consumeMessage(Message receivedMessage,
            MessageListener listener) {
        logger.debug("Consumption of message {} by listener {}", receivedMessage, listener);
        try {
            listener.consume(receivedMessage);
            handleLocalMessage(new SuccessfulConsumption(listener.getListenerId(), receivedMessage));
        } catch (Exception e) {
            logger.warn("Consumption of message {} by listener {} failed", receivedMessage, listener, e);
            handleLocalMessage(new FailedConsumption(listener.getListenerId(), receivedMessage, ExceptionUtils.getStackTrace(e)));
        }
        logger.debug("Message {} consumed by listener {}", receivedMessage, listener);
    }

    private void handleLocalMessage(Message message) {
        for (MessageListener listener : listenerRegistry
                .getListeners(new MessageListenerRoutingKey(message.getClass()))) {
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

    public void startReceiving() {
        if (started) {
            return;
        }
        actuallyStartReceiving();
        started = true;
    }

    protected abstract void actuallyStartReceiving();

    public void setListenerRegistry(MessageListenerRegistry listenerRegistry) {
        checkThat(value(listenerRegistry).notNull().because("Message listener registry cannot be null"));
        this.listenerRegistry = listenerRegistry;
    }

    public boolean isStarted() {
        return started;
    }
}
