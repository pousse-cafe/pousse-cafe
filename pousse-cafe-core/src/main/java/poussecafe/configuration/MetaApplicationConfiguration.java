package poussecafe.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import poussecafe.consequence.ConsequenceEmitter;
import poussecafe.consequence.ConsequenceReceiver;
import poussecafe.consequence.DefaultSourceSelector;
import poussecafe.consequence.InMemoryConsequenceQueue;
import poussecafe.consequence.Source;
import poussecafe.consequence.SourceSelector;
import poussecafe.journal.PollingPeriod;
import poussecafe.service.Workflow;
import poussecafe.storage.InMemoryStorage;
import poussecafe.storage.Storage;
import poussecafe.util.IdGenerator;

import static java.util.Arrays.asList;
import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class MetaApplicationConfiguration {

    private IdGenerator idGenerator;

    private SourceSelector sourceSelector;

    private ConsequenceJournalEntryConfiguration consequenceJournalEntryConfiguration;

    @SuppressWarnings("rawtypes")
    private Set<ActiveStorableConfiguration> aggregateConfigurations;

    private Set<Workflow> workflows;

    private Set<Object> services;

    private Set<ConsequenceEmitter> consequenceEmitters;

    private Set<ConsequenceReceiver> consequenceReceivers;

    private ProcessManagerConfiguration processManagerConfiguration;

    private List<Storage> storages;

    private Storage defaultStorage;

    public MetaApplicationConfiguration() {
        idGenerator = new IdGenerator();
        sourceSelector = new DefaultSourceSelector();
        consequenceJournalEntryConfiguration = new ConsequenceJournalEntryConfiguration();
        aggregateConfigurations = new HashSet<>();
        workflows = new HashSet<>();
        services = new HashSet<>();

        Set<InMemoryConsequenceQueue> defaultConsequenceQueues = defaultConsequenceQueues();
        consequenceEmitters = new HashSet<>(defaultConsequenceQueues);
        consequenceReceivers = new HashSet<>(defaultConsequenceQueues);

        processManagerConfiguration = new ProcessManagerConfiguration();

        storages = new ArrayList<>();
        defaultStorage = new InMemoryStorage();
    }

    protected IdGenerator idGenerator() {
        return new IdGenerator();
    }

    protected SourceSelector sourceSelectorSupplier() {
        return new DefaultSourceSelector();
    }

    private Set<InMemoryConsequenceQueue> defaultConsequenceQueues() {
        InMemoryConsequenceQueue commandQueue = new InMemoryConsequenceQueue(Source.DEFAULT_COMMAND_SOURCE);
        InMemoryConsequenceQueue eventQueue = new InMemoryConsequenceQueue(Source.DEFAULT_DOMAIN_EVENT_SOURCE);
        return new HashSet<>(asList(commandQueue, eventQueue));
    }

    @SuppressWarnings("rawtypes")
    public void registerAggregate(ActiveStorableConfiguration configuration) {
        aggregateConfigurations.add(configuration);
    }

    public void registerWorkflow(Workflow service) {
        workflows.add(service);
    }

    public void registerService(Object service) {
        services.add(service);
    }

    public void replaceEmitters(Set<ConsequenceEmitter> emitters) {
        consequenceEmitters.clear();
        consequenceEmitters.addAll(emitters);
    }

    public void replaceReceivers(Set<ConsequenceReceiver> receivers) {
        consequenceReceivers.clear();
        consequenceReceivers.addAll(receivers);
    }

    public IdGenerator getIdGenerator() {
        return idGenerator;
    }

    public SourceSelector getSourceSelector() {
        return sourceSelector;
    }

    public ConsequenceJournalEntryConfiguration getConsequenceJournalEntryConfiguration() {
        return consequenceJournalEntryConfiguration;
    }

    @SuppressWarnings("rawtypes")
    public Set<ActiveStorableConfiguration> getAggregateConfigurations() {
        return aggregateConfigurations;
    }

    public Set<Workflow> getWorkflows() {
        return workflows;
    }

    public Set<ConsequenceEmitter> getConsequenceEmitters() {
        return consequenceEmitters;
    }

    public Set<ConsequenceReceiver> getConsequenceReceivers() {
        return consequenceReceivers;
    }

    public PollingPeriod getCommandWatcherPollingPeriod() {
        return PollingPeriod.DEFAULT_POLLING_PERIOD;
    }

    public Set<Object> getServices() {
        return services;
    }

    public ProcessManagerConfiguration getProcessManagerConfiguration() {
        return processManagerConfiguration;
    }

    public void setStorages(List<Storage> storages) {
        this.storages.clear();
        this.storages.addAll(storages);
    }

    public List<Storage> getStorages() {
        return Collections.unmodifiableList(storages);
    }

    public void setDefaultStorage(Storage defaultStorage) {
        checkThat(value(defaultStorage).notNull().because("Default storage cannot be null"));
        this.defaultStorage = defaultStorage;
    }

    public Storage getDefaultStorage() {
        return defaultStorage;
    }
}
