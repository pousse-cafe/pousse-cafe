package poussecafe.configuration;

import java.util.Set;
import poussecafe.journal.CommandWatcher;
import poussecafe.journal.MessagingJournal;
import poussecafe.journal.MessageReplayer;
import poussecafe.journal.ConsumptionFailureRepository;
import poussecafe.journal.JournalEntry;
import poussecafe.journal.JournalEntryRepository;
import poussecafe.messaging.CommandProcessor;
import poussecafe.messaging.MessageSender;
import poussecafe.messaging.MessageListener;
import poussecafe.messaging.MessageListenerRegistry;
import poussecafe.messaging.MessageListenerRoutingKey;
import poussecafe.messaging.MessageReceiver;
import poussecafe.messaging.MessageRouter;
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

    private MessageListenerRegistry messageListenerRegistry;

    private MessageSenderRegistry messageSenderRegistry;

    private StorableRegistry storableRegistry;

    private ConsumptionFailureRepository consumptionFailureRepository;

    private MessagingJournal messagingJournal;

    private MessageRouter messageRouter;

    private CommandWatcher commandWatcher;

    private CommandProcessor commandProcessor;

    private MessageReplayer messageReplayer;

    private StorageServiceLocator storageServiceLocator;

    public MetaApplicationContext(MetaApplicationConfiguration configuration) {
        this.configuration = configuration;

        messageListenerRegistry = new MessageListenerRegistry();
        messageSenderRegistry = new MessageSenderRegistry();
        storableRegistry = new StorableRegistry();
        messagingJournal = new MessagingJournal();

        storageServiceLocator = new StorageServiceLocator();
        storageServiceLocator.setStorages(configuration.getStorages());
        storageServiceLocator.setDefaultStorage(configuration.getDefaultStorage());

        injector = new Injector();
        injector.registerInjectableService(configuration.getIdGenerator());

        configureContext();
        injector.injectDependencies();
        startMessageHandling();
    }

    private void configureContext() {
        configureAggregateServices();
        configureServices();
        configureProcessManagerServices();
        configureWorkflows();
        configureMessageSenders();
        configureMessageRouter();
        configureMessageEmissionPolicies();
        configureMessageJournal();
        configureMessageReceivers();
        configureCommandWatcher();
        configureCommandProcessor();
        configureMessageReplayer();
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
        workflowExplorer.setMessageListenerRegistry(messageListenerRegistry);
        workflowExplorer.setStorageServiceLocator(storageServiceLocator);

        for (Workflow workflow : configuration.getWorkflows()) {
            workflowExplorer.configureWorkflow(workflow);
            injector.addInjectionCandidate(workflow);
        }
    }

    private void configureMessageSenders() {
        for (MessageSender emitter : configuration.getMessageSenders()) {
            messageSenderRegistry.registerEmitter(emitter.getDestinationQueue(), emitter);
        }
    }

    private void configureMessageRouter() {
        messageRouter = new MessageRouter();
        messageRouter.setMessageSenderRegistry(messageSenderRegistry);
        messageRouter.setQueueSelector(configuration.getSourceSelector());
    }

    protected void configureMessageEmissionPolicies() {
        for (Storage storage : configuration.getStorages()) {
            storage.getMessageSendingPolicy().setMessageRouter(messageRouter);
        }
        configuration.getDefaultStorage().getMessageSendingPolicy().setMessageRouter(messageRouter);
    }

    private void configureMessageJournal() {
        MessagingJournalEntryConfiguration entryConfiguration = configuration
                .getMessagingJournalEntryConfiguration();
        entryConfiguration.setStorageServices(storageServiceLocator.locateStorageServices(JournalEntry.Data.class));
        messagingJournal.setEntryFactory(entryConfiguration.getConfiguredFactory().get());

        JournalEntryRepository journalEntryRepository = entryConfiguration.getConfiguredRepository().get();
        consumptionFailureRepository = new ConsumptionFailureRepository();
        consumptionFailureRepository.setJournalEntryRepository(journalEntryRepository);

        messagingJournal.setEntryRepository(journalEntryRepository);
        messagingJournal.setStorageServiceLocator(storageServiceLocator);
    }

    private void configureMessageReceivers() {
        for (MessageReceiver receiver : configuration.getMessageReceivers()) {
            receiver.setMessagingJournal(messagingJournal);
            receiver.setListenerRegistry(messageListenerRegistry);
        }
    }

    private void configureCommandWatcher() {
        commandWatcher = CommandWatcher.withPollingPeriod(configuration.getCommandWatcherPollingPeriod());
        commandWatcher.setMessagingJournal(messagingJournal);
        commandWatcher.setProcessManagerRepository(configuration.getProcessManagerConfiguration().repository().get());
    }

    private void configureCommandProcessor() {
        commandProcessor = new CommandProcessor();
        commandProcessor.setMessageRouter(messageRouter);
        commandProcessor.setCommandWatcher(commandWatcher);
    }

    private void configureMessageReplayer() {
        messageReplayer = new MessageReplayer();
        messageReplayer.setMessageRouter(messageRouter);
        messageReplayer.setConsumptionFailureRepository(consumptionFailureRepository);
    }

    private void startMessageHandling() {
        for (MessageReceiver receiver : configuration.getMessageReceivers()) {
            receiver.startReceiving();
        }
        commandWatcher.startWatching();
    }

    public StorableServices getStorableServices(Class<?> storableClass) {
        return storableRegistry.getServices(storableClass);
    }

    public Set<MessageListener> getMessageListeners(MessageListenerRoutingKey key) {
        return messageListenerRegistry.getListeners(key);
    }

    public CommandProcessor getCommandProcessor() {
        return commandProcessor;
    }

    public MessageRouter getMessageRouter() {
        return messageRouter;
    }

    public ConsumptionFailureRepository getConsumptionFailureRepository() {
        return consumptionFailureRepository;
    }

    public MessageReplayer getMessageReplayer() {
        return messageReplayer;
    }
}
