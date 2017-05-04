package poussecafe.configuration;

import poussecafe.messaging.MessageListenerRegistry;

class DomainEventListenerExplorer extends MessageListenerExplorer {

    DomainEventListenerExplorer(MessageListenerRegistry registry, Object service) {
        super(registry, service);
    }

    @Override
    protected void registerListener(MessageListenerEntry entry) {
        registry.registerDomainEventListener(entry);
    }

    @Override
    protected MessageListeningServiceSourceProcessor buildSourceProcessor(Object service) {
        return new DomainEventListeningServiceSourceProcessor(service);
    }

}
