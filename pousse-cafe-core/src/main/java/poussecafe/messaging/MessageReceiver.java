package poussecafe.messaging;

import poussecafe.journal.MessagingJournal;
import poussecafe.journal.SuccessfulConsumption;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public abstract class MessageReceiver {

    private MessageListenerRegistry listenerRegistry;

    private MessagingJournal messagingJournal;

    private boolean started;

    protected void onMessage(Message receivedMessage) {
        checkThat(value(receivedMessage).notNull().because("Received message cannot be null"));
        for (MessageListener listener : listenerRegistry
                .getListeners(new MessageListenerRoutingKey(receivedMessage.getClass()))) {
            if (!messagingJournal.isSuccessfullyConsumed(receivedMessage, listener.getListenerId())) {
                consumeMessage(receivedMessage, listener);
            } else {
                ignoreMessage(receivedMessage, listener);
            }
        }
    }

    private void consumeMessage(Message receivedMessage,
            MessageListener listener) {
        try {
            listener.consume(receivedMessage);
            messagingJournal.logSuccessfulConsumption(listener.getListenerId(),
                    new SuccessfulConsumption(receivedMessage));
        } catch (Exception e) {
            messagingJournal.logFailedConsumption(listener.getListenerId(), receivedMessage, e);
        }
    }

    private void ignoreMessage(Message receivedMessage,
            MessageListener listener) {
        messagingJournal.logIgnoredConsumption(listener.getListenerId(), receivedMessage);
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

    public void setMessagingJournal(MessagingJournal messageJournal) {
        checkThat(value(messageJournal).notNull().because("Message journal cannot be null"));
        messagingJournal = messageJournal;
    }

    public boolean isStarted() {
        return started;
    }
}
