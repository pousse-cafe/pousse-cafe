package poussecafe.context;

import java.util.List;
import poussecafe.messaging.MessageListener;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

abstract class MessageListenerExplorer {

    private Object service;

    MessageListenerExplorer(Object service) {
        setService(service);
    }

    private void setService(Object service) {
        checkThat(value(service).notNull().because("Service cannot be null"));
        this.service = service;
    }

    public List<MessageListener> discoverListeners() {
        MessageListenerDiscoverer sourceProcessor = buildSourceProcessor(service);
        return sourceProcessor.discoverListeners();
    }

    protected abstract MessageListenerDiscoverer buildSourceProcessor(Object service);
}
