package poussecafe.configuration;

import poussecafe.messaging.MessageListenerRegistry;

class CommandListenerExplorer extends MessageListenerExplorer {

    CommandListenerExplorer(MessageListenerRegistry registry, Object service) {
        super(registry, service);
    }

    @Override
    protected void registerListener(MessageListenerEntry entry) {
        registry.registerCommandListener(entry);
    }

    @Override
    protected MessageListeningServiceSourceProcessor buildSourceProcessor(Object service) {
        return new CommandListeningServiceSourceProcessor(service);
    }

}
