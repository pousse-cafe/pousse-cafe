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
import poussecafe.util.IdGenerator;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;

public abstract class ApplicationConfiguration {

    private Singleton<IdGenerator> idGenerator;

    private Singleton<SourceSelector> sourceSelector;

    private Singleton<StorageConfiguration> storageConfiguration;

    private Singleton<ConsequenceJournalEntryConfiguration> consequenceJournalEntryConfiguration;

    private Singleton<Set<ActiveStorableConfiguration<?, ?, ?, ?, ?>>> aggregateConfigurations;

    private Singleton<Set<ActiveStorableConfiguration<?, ?, ?, ?, ?>>> processManagerConfigurations;

    private Singleton<Set<Workflow>> workflows;

    private Singleton<Set<ConsequenceEmitter>> consequenceEmitters;

    private Singleton<Set<ConsequenceReceiver>> consequenceReceivers;

    private Singleton<Set<InMemoryConsequenceQueue>> defaultConsequenceQueues;

    public ApplicationConfiguration() {
        idGenerator = new Singleton<>(this::idGenerator);
        sourceSelector = new Singleton<>(this::sourceSelectorSupplier);
        storageConfiguration = new Singleton<>(this::storageConfiguration);
        consequenceJournalEntryConfiguration = new Singleton<>(this::consequenceJournalEntryConfiguration);
        aggregateConfigurations = new Singleton<>(this::aggregateConfigurations);
        processManagerConfigurations = new Singleton<>(this::processManagerConfigurations);
        workflows = new Singleton<>(this::workflows);
        consequenceEmitters = new Singleton<>(this::consequenceEmitters);
        consequenceReceivers = new Singleton<>(this::consequenceReceivers);
        defaultConsequenceQueues = new Singleton<>(this::defaultConsequenceQueues);
    }

    protected IdGenerator idGenerator() {
        return new IdGenerator();
    }

    protected SourceSelector sourceSelectorSupplier() {
        return new DefaultSourceSelector();
    }

    protected abstract StorageConfiguration storageConfiguration();

    protected abstract ConsequenceJournalEntryConfiguration consequenceJournalEntryConfiguration();

    protected abstract Set<ActiveStorableConfiguration<?, ?, ?, ?, ?>> aggregateConfigurations();

    protected abstract Set<ActiveStorableConfiguration<?, ?, ?, ?, ?>> processManagerConfigurations();

    protected abstract Set<Workflow> workflows();

    protected Set<ConsequenceEmitter> consequenceEmitters() {
        return defaultConsequenceQueues.get().stream().map(queue -> (ConsequenceEmitter) queue).collect(toSet());
    }

    protected Set<ConsequenceReceiver> consequenceReceivers() {
        return defaultConsequenceQueues.get().stream().map(queue -> (ConsequenceReceiver) queue).collect(toSet());
    }

    private Set<InMemoryConsequenceQueue> defaultConsequenceQueues() {
        InMemoryConsequenceQueue commandQueue = new InMemoryConsequenceQueue(Source.DEFAULT_COMMAND_SOURCE);
        InMemoryConsequenceQueue eventQueue = new InMemoryConsequenceQueue(Source.DEFAULT_DOMAIN_EVENT_SOURCE);
        return new HashSet<>(asList(commandQueue, eventQueue));
    }

    public Singleton<IdGenerator> getIdGenerator() {
        return idGenerator;
    }

    public Singleton<SourceSelector> getSourceSelector() {
        return sourceSelector;
    }

    public Singleton<StorageConfiguration> getStorageConfiguration() {
        return storageConfiguration;
    }

    public Singleton<ConsequenceJournalEntryConfiguration> getConsequenceJournalEntryConfiguration() {
        return consequenceJournalEntryConfiguration;
    }

    public Singleton<Set<ActiveStorableConfiguration<?, ?, ?, ?, ?>>> getAggregateConfigurations() {
        return aggregateConfigurations;
    }

    public Singleton<Set<ActiveStorableConfiguration<?, ?, ?, ?, ?>>> getProcessManagerConfigurations() {
        return processManagerConfigurations;
    }

    public Singleton<Set<Workflow>> getWorkflows() {
        return workflows;
    }

    public Singleton<Set<ConsequenceEmitter>> getConsequenceEmitters() {
        return consequenceEmitters;
    }

    public Singleton<Set<ConsequenceReceiver>> getConsequenceReceivers() {
        return consequenceReceivers;
    }

    public PollingPeriod getCommandWatcherPollingPeriod() {
        return PollingPeriod.DEFAULT_POLLING_PERIOD;
    }

}
