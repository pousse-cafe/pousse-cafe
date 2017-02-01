package poussecafe.configuration;

import poussecafe.consequence.ConsequenceListenerRegistry;

class CommandListenerExplorer extends ConsequenceListenerExplorer {

    CommandListenerExplorer(ConsequenceListenerRegistry registry, Object service) {
        super(registry, service);
    }

    @Override
    protected void registerListener(ConsequenceListenerEntry entry) {
        registry.registerCommandListener(entry);
    }

    @Override
    protected ConsequenceListeningServiceSourceProcessor buildSourceProcessor(Object service) {
        return new CommandListeningServiceSourceProcessor(service);
    }

}
