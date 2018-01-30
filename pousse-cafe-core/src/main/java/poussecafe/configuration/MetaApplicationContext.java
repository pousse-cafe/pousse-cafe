package poussecafe.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import poussecafe.exception.PousseCafeException;
import poussecafe.inmemory.InMemoryJournalEntryData;
import poussecafe.journal.ConsumptionFailureRepository;
import poussecafe.journal.InMemoryJournalEntryDataAccess;
import poussecafe.journal.JournalEntry;
import poussecafe.journal.JournalEntryFactory;
import poussecafe.journal.JournalEntryRepository;
import poussecafe.journal.MessageReplayer;
import poussecafe.journal.MessagingJournal;
import poussecafe.messaging.InMemoryMessageQueue;
import poussecafe.messaging.MessageListener;
import poussecafe.messaging.MessageListenerRegistry;
import poussecafe.messaging.MessageListenerRoutingKey;
import poussecafe.messaging.MessageReceiver;
import poussecafe.messaging.MessageSender;
import poussecafe.service.Process;
import poussecafe.storable.Environment;
import poussecafe.storable.IdentifiedStorableFactory;
import poussecafe.storable.IdentifiedStorableRepository;
import poussecafe.storable.PrimitiveFactory;
import poussecafe.storable.PrimitiveSpecification;
import poussecafe.storable.StorableDefinition;
import poussecafe.storable.StorableImplementation;
import poussecafe.storage.InMemoryStorage;
import poussecafe.storage.Storage;
import poussecafe.util.IdGenerator;

public class MetaApplicationContext {

    private Environment environment;

    private PrimitiveFactory primitiveFactory;

    private Injector injector;

    private MessageListenerRegistry messageListenerRegistry;

    private ConsumptionFailureRepository consumptionFailureRepository;

    private MessagingJournal messagingJournal;

    private MessageReplayer messageReplayer;

    private StorageServiceLocator storageServiceLocator;

    private Storage inMemoryStorage;

    private InMemoryMessageQueue inMemoryMessageQueue;

    private MessageSender messageSender;

    private MessageReceiver messageReceiver;

    private Map<Class<?>, StorableServices> storableServices = new HashMap<>();

    public MetaApplicationContext() {
        environment = new Environment();

        primitiveFactory = new PrimitiveFactory();
        primitiveFactory.setEnvironment(environment);

        messageListenerRegistry = new MessageListenerRegistry();
        messagingJournal = new MessagingJournal();

        storageServiceLocator = new StorageServiceLocator();
        storageServiceLocator.setEnvironment(environment);

        inMemoryStorage = new InMemoryStorage();
        configureDefaultEnvironment();

        inMemoryMessageQueue = new InMemoryMessageQueue();
        messageSender = inMemoryMessageQueue;
        messageReceiver = inMemoryMessageQueue;
    }

    private void configureDefaultEnvironment() {
        environment.defineService(IdGenerator.class);

        environment.defineStorable(new StorableDefinition.Builder()
                .withStorableClass(JournalEntry.class)
                .withFactoryClass(JournalEntryFactory.class)
                .withRepositoryClass(JournalEntryRepository.class)
                .build());
        environment.implementStorable(new StorableImplementation.Builder()
                .withStorableClass(JournalEntry.class)
                .withDataFactory(InMemoryJournalEntryData::new)
                .withDataAccessFactory(InMemoryJournalEntryDataAccess::new)
                .withStorage(inMemoryStorage)
                .build());
    }

    public Environment environment() {
        return environment;
    }

    public void start() {
        if(environment.isAbstract()) {
            throw new PousseCafeException("Cannot start meta-application with an abstract environment");
        }

        injector = new Injector();
        injector.registerInjectableService(new IdGenerator());

        configureContext();
        injector.injectDependencies();
        startMessageHandling();
    }

    private void configureContext() {
        configureStorables();
        configureServices();
        configureProcesses();
        configureMessageEmissionPolicies();
        configureMessageJournal();
        configureMessageReceivers();
        configureMessageReplayer();
    }

    protected void configureStorables() {
        for (Class<?> storableClass : environment.getDefinedStorables()) {
            configureStorable(storableClass);
        }
    }

    private void configureStorable(Class<?> storableClass) {
        StorableDefinition definition = environment.getStorableDefinition(storableClass);
        if(definition.hasFactory() && definition.hasRepository()) {
            IdentifiedStorableRepository repository = primitiveFactory.newPrimitive(PrimitiveSpecification.ofClass(definition.getRepositoryClass()));
            IdentifiedStorableFactory factory = primitiveFactory.newPrimitive(PrimitiveSpecification.ofClass(definition.getFactoryClass()));

            injector.registerInjectableService(repository);
            injector.registerInjectableService(factory);
            injector.addInjectionCandidate(factory);

            storableServices.put(storableClass, new StorableServices(storableClass, repository, factory));
        }
    }

    protected void configureServices() {
        for (Class<?> serviceClass : environment.getDefinedServices()) {
            Object service = newInstance(serviceClass);
            injector.registerInjectableService(service);
            injector.addInjectionCandidate(service);
        }
    }

    private Object newInstance(Class<?> serviceClass) {
        try {
            return serviceClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new PousseCafeException("Unable to instantiate service", e);
        }
    }

    protected void configureProcesses() {
        ProcessExplorer processExplorer = new ProcessExplorer();
        processExplorer.setMessageListenerRegistry(messageListenerRegistry);
        processExplorer.setStorageServiceLocator(storageServiceLocator);

        for (Class<?> processClass : environment.getDefinedProcesses()) {
            Process process = (Process) newInstance(processClass);
            processExplorer.configureProcess(process);
            injector.addInjectionCandidate(process);

            processes.put(processClass, process);
        }
    }

    private Map<Class<?>, Process> processes = new HashMap<>();

    protected void configureMessageEmissionPolicies() {
        for (Storage storage : environment.getStorages()) {
            storage.getMessageSendingPolicy().setMessageSender(messageSender);
        }
    }

    private void configureMessageJournal() {
        injector.addInjectionCandidate(messagingJournal);
        messagingJournal.setStorageServiceLocator(storageServiceLocator);

        consumptionFailureRepository = new ConsumptionFailureRepository();
        injector.addInjectionCandidate(consumptionFailureRepository);
    }

    private void configureMessageReceivers() {
        messageReceiver.setMessagingJournal(messagingJournal);
        messageReceiver.setListenerRegistry(messageListenerRegistry);
    }

    private void configureMessageReplayer() {
        messageReplayer = new MessageReplayer();
        messageReplayer.setMessageSender(messageSender);
        messageReplayer.setConsumptionFailureRepository(consumptionFailureRepository);
    }

    private void startMessageHandling() {
        messageReceiver.startReceiving();
    }

    public Set<MessageListener> getMessageListeners(MessageListenerRoutingKey key) {
        return messageListenerRegistry.getListeners(key);
    }

    public MessageSender getMessageSender() {
        return messageSender;
    }

    public ConsumptionFailureRepository getConsumptionFailureRepository() {
        return consumptionFailureRepository;
    }

    public MessageReplayer getMessageReplayer() {
        return messageReplayer;
    }

    public MessageReceiver getMessageReceiver() {
        return messageReceiver;
    }

    public Storage getInMemoryStorage() {
        return inMemoryStorage;
    }

    public StorableServices getStorableServices(Class<?> storableClass) {
        return storableServices.get(storableClass);
    }

    public InMemoryMessageQueue getInMemoryQueue() {
        return inMemoryMessageQueue;
    }

    public <T extends Process> T getProcess(Class<T> processClass) {
        return (T) processes.get(processClass);
    }
}
