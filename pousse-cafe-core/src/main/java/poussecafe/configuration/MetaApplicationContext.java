package poussecafe.configuration;

import java.util.Set;
import poussecafe.consequence.CommandProcessor;
import poussecafe.consequence.ConsequenceEmitter;
import poussecafe.consequence.ConsequenceListener;
import poussecafe.consequence.ConsequenceListenerRegistry;
import poussecafe.consequence.ConsequenceListenerRoutingKey;
import poussecafe.consequence.ConsequenceReceiver;
import poussecafe.consequence.ConsequenceRouter;
import poussecafe.journal.CommandWatcher;
import poussecafe.journal.ConsequenceJournal;
import poussecafe.journal.ConsequenceReplayer;
import poussecafe.journal.ConsumptionFailureRepository;
import poussecafe.journal.JournalEntry;
import poussecafe.journal.JournalEntryRepository;
import poussecafe.service.Workflow;
import poussecafe.storable.ActiveStorable;
import poussecafe.storable.ActiveStorableFactory;
import poussecafe.storable.ActiveStorableRepository;
import poussecafe.storable.StorableData;
import poussecafe.storage.Storage;

@SuppressWarnings("rawtypes")
public class MetaApplicationContext {

    private MetaApplicationConfiguration configuration;

    private Injector injector;

    private ConsequenceListenerRegistry consequenceListenerRegistry;

    private ConsequenceEmitterRegistry consequenceEmitterRegistry;

    private StorableRegistry storableRegistry;

    private ConsumptionFailureRepository consumptionFailureRepository;

    private ConsequenceJournal consequenceJournal;

    private ConsequenceRouter consequenceRouter;

    private CommandWatcher commandWatcher;

    private CommandProcessor commandProcessor;

    private ConsequenceReplayer consequenceReplayer;

    private StorageServiceLocator storageServiceLocator;

    public MetaApplicationContext(MetaApplicationConfiguration configuration) {
        this.configuration = configuration;

        consequenceListenerRegistry = new ConsequenceListenerRegistry();
        consequenceEmitterRegistry = new ConsequenceEmitterRegistry();
        storableRegistry = new StorableRegistry();
        consequenceJournal = new ConsequenceJournal();

        storageServiceLocator = new StorageServiceLocator();
        storageServiceLocator.setStorages(configuration.getStorages());
        storageServiceLocator.setDefaultStorage(configuration.getDefaultStorage());

        injector = new Injector();
        injector.registerInjectableService(configuration.getIdGenerator());

        configureContext();
        injector.injectDependencies();
        startConsequenceHandling();
    }

    private void configureContext() {
        configureAggregateServices();
        configureServices();
        configureProcessManagerServices();
        configureWorkflows();
        configureConsequenceEmitters();
        configureConsequenceRouter();
        configureConsequenceEmissionPolicies();
        configureConsequenceJournal();
        configureConsequenceReceivers();
        configureCommandWatcher();
        configureCommandProcessor();
        configureConsequenceReplayer();
    }

    protected void configureAggregateServices() {
        Set<ActiveStorableConfiguration> configurations = configuration.getAggregateConfigurations();
        configureStorableServices(configurations);
    }

    protected void configureStorableServices(Set<ActiveStorableConfiguration> configurations) {
        for (ActiveStorableConfiguration<?, ?, ?, ?, ?> storableConfiguration : configurations) {
            configureStorableService(storableConfiguration);
        }
    }

    private <K, A extends ActiveStorable<K, D>, D extends StorableData<K>, F extends ActiveStorableFactory<K, A, D>, R extends ActiveStorableRepository<A, K, D>> void configureStorableService(
            ActiveStorableConfiguration<K, A, D, F, R> storableConfiguration) {
        Class<A> storableClass = storableConfiguration.getStorableClass();
        Class<D> dataClass = storableConfiguration.getDataClass();
        StorageServices<K, D> storageServices = storageServiceLocator.locateStorageServices(dataClass);
        storableConfiguration.setStorageServices(storageServices);
        ActiveStorableRepository<A, K, D> repository = storableConfiguration.getConfiguredRepository().get();
        ActiveStorableFactory<K, A, D> factory = storableConfiguration.getConfiguredFactory().get();

        storableRegistry.registerServices(new StorableServices(storableClass, repository, factory));

        injector.registerInjectableService(repository);
        injector.registerInjectableService(factory);
        injector.addInjectionCandidate(factory);
    }

    protected void configureServices() {
        for (Object service : configuration.getServices()) {
            injector.registerInjectableService(service);
            injector.addInjectionCandidate(service);
        }
    }

    protected void configureProcessManagerServices() {
        configureStorableService(configuration.getProcessManagerConfiguration());
    }

    protected void configureWorkflows() {
        WorkflowExplorer workflowExplorer = new WorkflowExplorer();
        workflowExplorer.setConsequenceListenerRegistry(consequenceListenerRegistry);
        workflowExplorer.setStorageServiceLocator(storageServiceLocator);

        for (Workflow workflow : configuration.getWorkflows()) {
            workflowExplorer.configureWorkflow(workflow);
            injector.addInjectionCandidate(workflow);
        }
    }

    private void configureConsequenceEmitters() {
        for (ConsequenceEmitter emitter : configuration.getConsequenceEmitters()) {
            consequenceEmitterRegistry.registerEmitter(emitter.getDestinationSource(), emitter);
        }
    }

    private void configureConsequenceRouter() {
        consequenceRouter = new ConsequenceRouter();
        consequenceRouter.setConsequenceEmitterRegistry(consequenceEmitterRegistry);
        consequenceRouter.setSourceSelector(configuration.getSourceSelector());
    }

    protected void configureConsequenceEmissionPolicies() {
        for (Storage storage : configuration.getStorages()) {
            storage.getConsequenceEmissionPolicy().setConsequenceRouter(consequenceRouter);
        }
        configuration.getDefaultStorage().getConsequenceEmissionPolicy().setConsequenceRouter(consequenceRouter);
    }

    private void configureConsequenceJournal() {
        ConsequenceJournalEntryConfiguration entryConfiguration = configuration
                .getConsequenceJournalEntryConfiguration();
        entryConfiguration.setStorageServices(storageServiceLocator.locateStorageServices(JournalEntry.Data.class));
        consequenceJournal.setEntryFactory(entryConfiguration.getConfiguredFactory().get());

        JournalEntryRepository journalEntryRepository = entryConfiguration.getConfiguredRepository().get();
        consumptionFailureRepository = new ConsumptionFailureRepository();
        consumptionFailureRepository.setJournalEntryRepository(journalEntryRepository);

        consequenceJournal.setEntryRepository(journalEntryRepository);
        consequenceJournal.setStorageServiceLocator(storageServiceLocator);
    }

    private void configureConsequenceReceivers() {
        for (ConsequenceReceiver receiver : configuration.getConsequenceReceivers()) {
            receiver.setConsequenceJournal(consequenceJournal);
            receiver.setListenerRegistry(consequenceListenerRegistry);
        }
    }

    private void configureCommandWatcher() {
        commandWatcher = CommandWatcher.withPollingPeriod(configuration.getCommandWatcherPollingPeriod());
        commandWatcher.setConsequenceJournal(consequenceJournal);
        commandWatcher.setProcessManagerRepository(configuration.getProcessManagerConfiguration().repository().get());
    }

    private void configureCommandProcessor() {
        commandProcessor = new CommandProcessor();
        commandProcessor.setConsequenceRouter(consequenceRouter);
        commandProcessor.setCommandWatcher(commandWatcher);
    }

    private void configureConsequenceReplayer() {
        consequenceReplayer = new ConsequenceReplayer();
        consequenceReplayer.setConsequenceRouter(consequenceRouter);
        consequenceReplayer.setConsumptionFailureRepository(consumptionFailureRepository);
    }

    private void startConsequenceHandling() {
        for (ConsequenceReceiver receiver : configuration.getConsequenceReceivers()) {
            receiver.startReceiving();
        }
        commandWatcher.startWatching();
    }

    public StorableServices getStorableServices(Class<?> storableClass) {
        return storableRegistry.getServices(storableClass);
    }

    public Set<ConsequenceListener> getConsequenceListeners(ConsequenceListenerRoutingKey key) {
        return consequenceListenerRegistry.getListeners(key);
    }

    public CommandProcessor getCommandProcessor() {
        return commandProcessor;
    }

    public ConsequenceRouter getConsequenceRouter() {
        return consequenceRouter;
    }

    public ConsumptionFailureRepository getConsumptionFailureRepository() {
        return consumptionFailureRepository;
    }

    public ConsequenceReplayer getConsequenceReplayer() {
        return consequenceReplayer;
    }
}
