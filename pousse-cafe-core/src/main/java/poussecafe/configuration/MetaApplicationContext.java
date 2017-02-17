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
import poussecafe.journal.JournalEntryRepository;
import poussecafe.service.Workflow;
import poussecafe.storable.ActiveStorableFactory;
import poussecafe.storable.ActiveStorableRepository;
import poussecafe.storage.ConsequenceEmissionPolicy;
import poussecafe.storage.TransactionRunner;

@SuppressWarnings("rawtypes")
public class MetaApplicationContext {

    private MetaApplicationConfiguration configuration;

    private Injector injector;

    private ConsequenceEmissionPolicy consequenceEmissionPolicy;

    private TransactionRunner transactionRunner;

    private ConsequenceListenerRegistry consequenceListenerRegistry;

    private ConsequenceEmitterRegistry consequenceEmitterRegistry;

    private StorableRegistry storableRegistry;

    private JournalEntryRepository journalEntryRepository;

    private ConsequenceJournal consequenceJournal;

    private ConsequenceRouter consequenceRouter;

    private CommandWatcher commandWatcher;

    private CommandProcessor commandProcessor;

    private ConsequenceReplayer consequenceReplayer;

    public MetaApplicationContext(MetaApplicationConfiguration configuration) {
        this.configuration = configuration;

        StorageConfiguration storageConfiguration = configuration.getStorageConfiguration();
        consequenceEmissionPolicy = storageConfiguration.getConsequenceEmissionPolicy().get();
        transactionRunner = storageConfiguration.getTransactionRunner().get();

        consequenceListenerRegistry = new ConsequenceListenerRegistry();
        consequenceEmitterRegistry = new ConsequenceEmitterRegistry();
        storableRegistry = new StorableRegistry();
        consequenceJournal = new ConsequenceJournal();

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
        configureConsequenceEmissionPolicy();
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

    private void configureStorableService(ActiveStorableConfiguration<?, ?, ?, ?, ?> storableConfiguration) {
        storableConfiguration.setConsequenceEmissionPolicy(consequenceEmissionPolicy);
        Class<?> storableClass = storableConfiguration.getStorableClass();
        ActiveStorableRepository<?, ?, ?> repository = storableConfiguration.getConfiguredRepository().get();
        ActiveStorableFactory<?, ?, ?> factory = storableConfiguration.getConfiguredFactory().get();

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
        workflowExplorer.setTransactionRunner(transactionRunner);

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

    protected void configureConsequenceEmissionPolicy() {
        consequenceEmissionPolicy.setConsequenceRouter(consequenceRouter);
    }

    private void configureConsequenceJournal() {
        ConsequenceJournalEntryConfiguration entryConfiguration = configuration
                .getConsequenceJournalEntryConfiguration();
        consequenceJournal.setEntryFactory(entryConfiguration.getConfiguredFactory().get());
        journalEntryRepository = entryConfiguration.getConfiguredRepository().get();
        consequenceJournal.setEntryRepository(journalEntryRepository);
        consequenceJournal.setTransactionRunner(transactionRunner);
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
        consequenceReplayer.setJournalEntryRepository(journalEntryRepository);
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

    public JournalEntryRepository getJournalEntryRepository() {
        return journalEntryRepository;
    }

    public ConsequenceReplayer getConsequenceReplayer() {
        return consequenceReplayer;
    }
}
