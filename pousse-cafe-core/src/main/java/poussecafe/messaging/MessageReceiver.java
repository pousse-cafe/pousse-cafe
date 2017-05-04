package poussecafe.messaging;

import poussecafe.journal.MessagingJournal;
import poussecafe.journal.SuccessfulConsumption;
import poussecafe.process.ProcessManagerKey;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public abstract class MessageReceiver {

    private Queue messageSource;

    private MessageListenerRegistry listenerRegistry;

    private MessagingJournal messagingJournal;

    private boolean started;

    protected MessageReceiver(Queue messageSource) {
        setMessageSource(messageSource);
    }

    private void setMessageSource(Queue messageSource) {
        checkThat(value(messageSource).notNull().because("Message source cannot be null"));
        this.messageSource = messageSource;
    }

    protected void onMessage(Message receivedMessage) {
        checkThat(value(receivedMessage).notNull().because("Received message cannot be null"));
        for (MessageListener listener : listenerRegistry
                .getListeners(new MessageListenerRoutingKey(messageSource, receivedMessage.getClass()))) {
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
            ProcessManagerKey createdProcessManagerKey = listener.consume(receivedMessage);
            messagingJournal.logSuccessfulConsumption(listener.getListenerId(),
                    new SuccessfulConsumption(receivedMessage, createdProcessManagerKey));
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

    public Queue getSource() {
        return messageSource;
    }

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
