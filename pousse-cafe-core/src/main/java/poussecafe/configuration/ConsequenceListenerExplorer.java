package poussecafe.configuration;

import java.util.List;
import poussecafe.consequence.ConsequenceListenerRegistry;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

abstract class ConsequenceListenerExplorer {

    protected ConsequenceListenerRegistry registry;

    private Object service;

    ConsequenceListenerExplorer(ConsequenceListenerRegistry registry, Object service) {
        setRegistry(registry);
        setService(service);
    }

    private void setRegistry(ConsequenceListenerRegistry registry) {
        checkThat(value(registry).notNull().because("Consequence listener registry cannot be null"));
        this.registry = registry;
    }

    private void setService(Object service) {
        checkThat(value(service).notNull().because("Service cannot be null"));
        this.service = service;
    }

    public void discoverListeners() {
        ConsequenceListeningServiceSourceProcessor sourceProcessor = buildSourceProcessor(service);
        List<ConsequenceListenerEntry> domainEventListenersEntries = sourceProcessor.discoverListeners();
        for (ConsequenceListenerEntry entry : domainEventListenersEntries) {
            registerListener(entry);
        }
    }

    protected abstract ConsequenceListeningServiceSourceProcessor buildSourceProcessor(Object service);

    protected abstract void registerListener(ConsequenceListenerEntry entry);
}
