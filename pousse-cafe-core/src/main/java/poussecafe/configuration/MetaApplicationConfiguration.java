package poussecafe.configuration;

import java.util.HashSet;
import java.util.Set;
import poussecafe.consequence.ConsequenceEmitter;
import poussecafe.consequence.ConsequenceReceiver;
import poussecafe.consequence.DefaultSourceSelector;
import poussecafe.consequence.InMemoryConsequenceQueue;
import poussecafe.consequence.Source;
import poussecafe.consequence.SourceSelector;
import poussecafe.journal.PollingPeriod;
import poussecafe.service.Workflow;
import poussecafe.storage.TransactionLessStorage;
import poussecafe.util.IdGenerator;

import static java.util.Arrays.asList;

public class MetaApplicationConfiguration {

    private IdGenerator idGenerator;

    private SourceSelector sourceSelector;

    private StorageConfiguration storageConfiguration;

    private ConsequenceJournalEntryConfiguration consequenceJournalEntryConfiguration;

    @SuppressWarnings("rawtypes")
    private Set<ActiveStorableConfiguration> aggregateConfigurations;

    @SuppressWarnings("rawtypes")
    private Set<ActiveStorableConfiguration> processManagerConfigurations;

    private Set<Workflow> workflows;

    private Set<Object> services;

    private Set<ConsequenceEmitter> consequenceEmitters;

    private Set<ConsequenceReceiver> consequenceReceivers;

    public MetaApplicationConfiguration() {
        idGenerator = new IdGenerator();
        sourceSelector = new DefaultSourceSelector();
        storageConfiguration = new TransactionLessStorage();
        consequenceJournalEntryConfiguration = new InMemoryConsequenceJournalEntryConfiguration();
        aggregateConfigurations = new HashSet<>();
        processManagerConfigurations = new HashSet<>();
        workflows = new HashSet<>();
        services = new HashSet<>();

        Set<InMemoryConsequenceQueue> defaultConsequenceQueues = defaultConsequenceQueues();
        consequenceEmitters = new HashSet<>(defaultConsequenceQueues);
        consequenceReceivers = new HashSet<>(defaultConsequenceQueues);
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

    @SuppressWarnings("rawtypes")
    public void registerProcessManagerConfiguration(ActiveStorableConfiguration configuration) {
        processManagerConfigurations.add(configuration);
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

    public StorageConfiguration getStorageConfiguration() {
        return storageConfiguration;
    }

    public ConsequenceJournalEntryConfiguration getConsequenceJournalEntryConfiguration() {
        return consequenceJournalEntryConfiguration;
    }

    @SuppressWarnings("rawtypes")
    public Set<ActiveStorableConfiguration> getAggregateConfigurations() {
        return aggregateConfigurations;
    }

    @SuppressWarnings("rawtypes")
    public Set<ActiveStorableConfiguration> getProcessManagerConfigurations() {
        return processManagerConfigurations;
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

}
