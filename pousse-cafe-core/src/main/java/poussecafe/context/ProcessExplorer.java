package poussecafe.context;

import poussecafe.messaging.MessageListenerRegistry;
import poussecafe.service.DomainProcess;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class ProcessExplorer {

    private MessageListenerRegistry registry;

    public void setMessageListenerRegistry(MessageListenerRegistry registry) {
        checkThat(value(registry).notNull().because("Message listener registry cannot be null"));
        this.registry = registry;
    }

    public void discoverListeners(DomainProcess service) {
        checkThat(value(service).notNull().because("Service cannot be null"));
        DomainEventListenerExplorer explorer = new DomainEventListenerExplorer(registry, service);
        explorer.discoverListeners();
    }

}
