package poussecafe.configuration;

import java.util.List;
import poussecafe.messaging.MessageListenerRegistry;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

abstract class MessageListenerExplorer {

    protected MessageListenerRegistry registry;

    private Object service;

    MessageListenerExplorer(MessageListenerRegistry registry, Object service) {
        setRegistry(registry);
        setService(service);
    }

    private void setRegistry(MessageListenerRegistry registry) {
        checkThat(value(registry).notNull().because("Message listener registry cannot be null"));
        this.registry = registry;
    }

    private void setService(Object service) {
        checkThat(value(service).notNull().because("Service cannot be null"));
        this.service = service;
    }

    public void discoverListeners() {
        MessageListeningServiceSourceProcessor sourceProcessor = buildSourceProcessor(service);
        List<MessageListenerEntry> messageListenersEntries = sourceProcessor.discoverListeners();
        for (MessageListenerEntry entry : messageListenersEntries) {
            registerListener(entry);
        }
    }

    protected abstract MessageListeningServiceSourceProcessor buildSourceProcessor(Object service);

    protected abstract void registerListener(MessageListenerEntry entry);
}
