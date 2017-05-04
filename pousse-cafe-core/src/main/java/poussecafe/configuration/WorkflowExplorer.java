package poussecafe.configuration;

import java.util.HashSet;
import java.util.Set;
import poussecafe.messaging.MessageListenerRegistry;
import poussecafe.service.Workflow;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.hasItem;
import static poussecafe.check.Predicates.not;

public class WorkflowExplorer {

    private MessageListenerRegistry registry;

    private StorageServiceLocator storageServiceLocator;

    private Set<Workflow> configuredServices;

    public WorkflowExplorer() {
        configuredServices = new HashSet<>();
    }

    public void setMessageListenerRegistry(MessageListenerRegistry registry) {
        checkThat(value(registry).notNull().because("Message listener registry cannot be null"));
        this.registry = registry;
    }

    public void setStorageServiceLocator(StorageServiceLocator storageServiceLocator) {
        checkThat(value(storageServiceLocator).notNull().because("Transaction runner cannot be null"));
        this.storageServiceLocator = storageServiceLocator;
    }

    public void configureWorkflow(Workflow service) {
        checkThat(value(service).notNull().because("Service cannot be null"));
        checkThat(value(configuredServices).verifies(not(hasItem(service)))
                .because("Service cannot be configured more than once"));

        service.setStorageServiceLocator(storageServiceLocator);

        discoverDomainEventListeners(service);
        discoverCommandListeners(service);

        configuredServices.add(service);
    }

    protected void discoverDomainEventListeners(Object service) {
        DomainEventListenerExplorer explorer = new DomainEventListenerExplorer(registry, service);
        explorer.discoverListeners();
    }

    protected void discoverCommandListeners(Object service) {
        CommandListenerExplorer explorer = new CommandListenerExplorer(registry, service);
        explorer.discoverListeners();
    }

}
