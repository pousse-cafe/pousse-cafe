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
import poussecafe.service.Workflow;
import poussecafe.storable.ActiveStorableFactory;
import poussecafe.storable.ActiveStorableRepository;
import poussecafe.storage.ConsequenceEmissionPolicy;
import poussecafe.storage.TransactionRunner;

public class ApplicationContext {

    private ApplicationConfiguration configuration;

    private Injector injector;

    private ConsequenceEmissionPolicy consequenceEmissionPolicy;

    private TransactionRunner transactionRunner;

    private ConsequenceListenerRegistry consequenceListenerRegistry;

    private ConsequenceEmitterRegistry consequenceEmitterRegistry;

    private StorableRegistry storableRegistry;

    private ConsequenceJournal consequenceJournal;

    private ConsequenceRouter consequenceRouter;

    private CommandWatcher commandWatcher;

    private CommandProcessor commandProcessor;

    public ApplicationContext(ApplicationConfiguration configuration) {
        this.configuration = configuration;

        StorageConfiguration storageConfiguration = configuration.getStorageConfiguration().get();
        consequenceEmissionPolicy = storageConfiguration.getConsequenceEmissionPolicy().get();
        transactionRunner = storageConfiguration.getTransactionRunner().get();

        consequenceListenerRegistry = new ConsequenceListenerRegistry();
        consequenceEmitterRegistry = new ConsequenceEmitterRegistry();
        storableRegistry = new StorableRegistry();
        consequenceJournal = new ConsequenceJournal();

        injector = new Injector();
        injector.registerInjectableService(configuration.getIdGenerator().get());

        configureContext();
        injector.injectDependencies();
        startConsequenceHandling();
    }

    private void configureContext() {
        configureAggregateServices();
        configureProcessManagerServices();
        configureWorkflows();
        configureConsequenceEmitters();
        configureConsequenceRouter();
        configureConsequenceEmissionPolicy();
        configureConsequenceJournal();
        configureConsequenceReceivers();
        configureCommandWatcher();
        configureCommandProcessor();
    }

    protected void configureAggregateServices() {
        Set<ActiveStorableConfiguration<?, ?, ?, ?, ?>> configurations = configuration
                .getAggregateConfigurations()
                .get();
        configureStorableServices(configurations);
    }

    protected void configureStorableServices(Set<ActiveStorableConfiguration<?, ?, ?, ?, ?>> configurations) {
        for (ActiveStorableConfiguration<?, ?, ?, ?, ?> storableConfiguration : configurations) {
            storableConfiguration.setConsequenceEmissionPolicy(consequenceEmissionPolicy);
            Class<?> storableClass = storableConfiguration.getStorableClass();
            ActiveStorableRepository<?, ?, ?> repository = storableConfiguration.getConfiguredRepository().get();
            ActiveStorableFactory<?, ?, ?> factory = storableConfiguration.getConfiguredFactory().get();

            storableRegistry.registerServices(new StorableServices(storableClass, repository, factory));

            injector.registerInjectableService(repository);
            injector.registerInjectableService(factory);
        }
    }

    protected void configureProcessManagerServices() {
        Set<ActiveStorableConfiguration<?, ?, ?, ?, ?>> configurations = configuration
                .getProcessManagerConfigurations()
                .get();
        configureStorableServices(configurations);
    }

    protected void configureWorkflows() {
        WorkflowExplorer workflowExplorer = new WorkflowExplorer();
        workflowExplorer.setConsequenceListenerRegistry(consequenceListenerRegistry);
        workflowExplorer.setTransactionRunner(transactionRunner);

        for (Workflow workflow : configuration.getWorkflows().get()) {
            workflowExplorer.configureWorkflow(workflow);
            injector.addInjectionCandidate(workflow);
        }
    }

    private void configureConsequenceEmitters() {
        for (ConsequenceEmitter emitter : configuration.getConsequenceEmitters().get()) {
            consequenceEmitterRegistry.registerEmitter(emitter.getDestinationSource(), emitter);
        }
    }

    private void configureConsequenceRouter() {
        consequenceRouter = new ConsequenceRouter();
        consequenceRouter.setConsequenceEmitterRegistry(consequenceEmitterRegistry);
        consequenceRouter.setSourceSelector(configuration.getSourceSelector().get());
    }

    protected void configureConsequenceEmissionPolicy() {
        consequenceEmissionPolicy.setConsequenceRouter(consequenceRouter);
    }

    private void configureConsequenceJournal() {
        ConsequenceJournalEntryConfiguration entryConfiguration = configuration
                .getConsequenceJournalEntryConfiguration()
                .get();
        consequenceJournal.setEntryFactory(entryConfiguration.getConfiguredFactory().get());
        consequenceJournal.setEntryRepository(entryConfiguration.getConfiguredRepository().get());
        consequenceJournal.setTransactionRunner(transactionRunner);
    }

    private void configureConsequenceReceivers() {
        for (ConsequenceReceiver receiver : configuration.getConsequenceReceivers().get()) {
            receiver.setConsequenceJournal(consequenceJournal);
            receiver.setListenerRegistry(consequenceListenerRegistry);
        }
    }

    private void configureCommandWatcher() {
        commandWatcher = CommandWatcher.withPollingPeriod(configuration.getCommandWatcherPollingPeriod());
        commandWatcher.setConsequenceJournal(consequenceJournal);
    }

    private void configureCommandProcessor() {
        commandProcessor = new CommandProcessor();
        commandProcessor.setConsequenceRouter(consequenceRouter);
        commandProcessor.setCommandWatcher(commandWatcher);
    }

    private void startConsequenceHandling() {
        for (ConsequenceReceiver receiver : configuration.getConsequenceReceivers().get()) {
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
}
