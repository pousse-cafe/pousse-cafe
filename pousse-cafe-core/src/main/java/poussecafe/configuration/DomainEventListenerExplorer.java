package poussecafe.configuration;

import poussecafe.consequence.ConsequenceListenerRegistry;

class DomainEventListenerExplorer extends ConsequenceListenerExplorer {

    DomainEventListenerExplorer(ConsequenceListenerRegistry registry, Object service) {
        super(registry, service);
    }

    @Override
    protected void registerListener(ConsequenceListenerEntry entry) {
        registry.registerDomainEventListener(entry);
    }

    @Override
    protected ConsequenceListeningServiceSourceProcessor buildSourceProcessor(Object service) {
        return new DomainEventListeningServiceSourceProcessor(service);
    }

}
