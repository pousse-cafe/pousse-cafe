package poussecafe.configuration;

import java.util.HashSet;
import java.util.Set;
import poussecafe.consequence.ConsequenceListenerRegistry;
import poussecafe.service.Workflow;
import poussecafe.storage.TransactionRunner;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.hasItem;
import static poussecafe.check.Predicates.not;

public class WorkflowExplorer {

    private ConsequenceListenerRegistry registry;

    private TransactionRunner transactionRunner;

    private Set<Workflow> configuredServices;

    public WorkflowExplorer() {
        configuredServices = new HashSet<>();
    }

    public void setConsequenceListenerRegistry(ConsequenceListenerRegistry registry) {
        checkThat(value(registry).notNull().because("Consequence listener registry cannot be null"));
        this.registry = registry;
    }

    public void setTransactionRunner(TransactionRunner transactionRunner) {
        checkThat(value(transactionRunner).notNull().because("Transaction runner cannot be null"));
        this.transactionRunner = transactionRunner;
    }

    public void configureWorkflow(Workflow service) {
        checkThat(value(service).notNull().because("Service cannot be null"));
        checkThat(value(configuredServices).verifies(not(hasItem(service)))
                .because("Service cannot be configured more than once"));

        service.setTransactionRunner(transactionRunner);

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
