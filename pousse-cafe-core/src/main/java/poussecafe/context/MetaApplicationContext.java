package poussecafe.context;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import poussecafe.exception.PousseCafeException;
import poussecafe.journal.ConsumptionFailureRepository;
import poussecafe.journal.MessageReplayer;
import poussecafe.journal.MessagingJournal;
import poussecafe.messaging.InMemoryMessageQueue;
import poussecafe.messaging.MessageAdapter;
import poussecafe.messaging.MessageListener;
import poussecafe.messaging.MessageListenerRegistry;
import poussecafe.messaging.MessageListenerRoutingKey;
import poussecafe.messaging.MessageReceiver;
import poussecafe.messaging.MessageSender;
import poussecafe.service.DomainProcess;
import poussecafe.storable.Environment;
import poussecafe.storable.IdentifiedStorable;
import poussecafe.storable.IdentifiedStorableData;
import poussecafe.storable.IdentifiedStorableFactory;
import poussecafe.storable.IdentifiedStorableRepository;
import poussecafe.storable.PrimitiveFactory;
import poussecafe.storable.PrimitiveSpecification;
import poussecafe.storable.StorableDefinition;
import poussecafe.storable.StorableImplementation;
import poussecafe.storage.Storage;
import poussecafe.util.IdGenerator;

import static java.util.Collections.unmodifiableCollection;

public class MetaApplicationContext {

    private Environment environment;

    private PrimitiveFactory primitiveFactory;

    private Injector injector;

    private MessageListenerRegistry messageListenerRegistry;

    private ConsumptionFailureRepository consumptionFailureRepository;

    private MessagingJournal messagingJournal;

    private MessageReplayer messageReplayer;

    private StorageServiceLocator storageServiceLocator;

    private InMemoryMessageQueue inMemoryMessageQueue;

    private MessageSender messageSender;

    private MessageReceiver messageReceiver;

    private Map<Class<?>, StorableServices> storableServices = new HashMap<>();

    private MessageAdapter messageAdapter;

    public MetaApplicationContext() {
        environment = new Environment();

        primitiveFactory = new PrimitiveFactory();
        primitiveFactory.setEnvironment(environment);

        messageListenerRegistry = new MessageListenerRegistry();
        messagingJournal = new MessagingJournal();

        storageServiceLocator = new StorageServiceLocator();
        storageServiceLocator.setEnvironment(environment);

        inMemoryMessageQueue = new InMemoryMessageQueue();
        messageSender = inMemoryMessageQueue;
        messageReceiver = inMemoryMessageQueue;

        messageAdapter = new JacksonMessageAdapter();
    }

    public Environment environment() {
        return environment;
    }

    public void loadBundle(MetaApplicationBundle bundle) {
        for(StorableDefinition definition : bundle.getDefinitions()) {
            environment.defineStorable(definition);
        }
        for(StorableImplementation implementation : bundle.getImplementations()) {
            environment.implementStorable(implementation);
        }
        for(Class<? extends DomainProcess> processClass : bundle.getProcesses()) {
            environment.defineProcess(processClass);
        }
        for(Class<?> serviceClass : bundle.getServices()) {
            environment.defineService(serviceClass);
        }
    }

    public void start() {
        if(environment.isAbstract()) {
            throw new PousseCafeException("Cannot start meta-application with an abstract environment");
        }

        injector = new Injector();
        injector.registerInjectableService(new IdGenerator());
        injector.registerInjectableService(environment);
        injector.registerInjectableService(MessageAdapter.class, messageAdapter);
        injector.registerInjectableService(storageServiceLocator);
        injector.registerInjectableService(messageListenerRegistry);

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

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void configureStorable(Class<?> storableClass) {
        StorableDefinition definition = environment.getStorableDefinition(storableClass);
        if(definition.hasFactory() && definition.hasRepository()) {
            IdentifiedStorableRepository repository = (IdentifiedStorableRepository) primitiveFactory.newPrimitive(PrimitiveSpecification.ofClass(definition.getRepositoryClass()));
            IdentifiedStorableFactory factory = (IdentifiedStorableFactory) primitiveFactory.newPrimitive(PrimitiveSpecification.ofClass(definition.getFactoryClass()));

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
            services.put(serviceClass, service);
        }
    }

    private Map<Class<?>, Object> services = new HashMap<>();

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

        for (Class<?> processClass : environment.getDefinedProcesses()) {
            DomainProcess process = (DomainProcess) newInstance(processClass);
            processExplorer.discoverListeners(process);
            injector.addInjectionCandidate(process);
            processes.put(processClass, process);
        }
    }

    private Map<Class<?>, DomainProcess> processes = new HashMap<>();

    protected void configureMessageEmissionPolicies() {
        for (Storage storage : environment.getStorages()) {
            storage.getMessageSendingPolicy().setMessageSender(messageSender);
        }
    }

    private void configureMessageJournal() {
        injector.addInjectionCandidate(messagingJournal);
        injector.registerInjectableService(messagingJournal);

        consumptionFailureRepository = new ConsumptionFailureRepository();
        injector.addInjectionCandidate(consumptionFailureRepository);
        injector.registerInjectableService(consumptionFailureRepository);
    }

    private void configureMessageReceivers() {
        injector.addInjectionCandidate(messageReceiver);
    }

    private void configureMessageReplayer() {
        messageReplayer = new MessageReplayer();
        injector.addInjectionCandidate(messageReplayer);
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

    public StorableServices getStorableServices(Class<?> storableClass) {
        return storableServices.get(storableClass);
    }

    public InMemoryMessageQueue getInMemoryQueue() {
        return inMemoryMessageQueue;
    }

    public <T extends DomainProcess> T getDomainProcess(Class<T> processClass) {
        return (T) processes.get(processClass);
    }

    public <T extends IdentifiedStorableRepository<A, K, D>, A extends IdentifiedStorable<K, D>, K, D extends IdentifiedStorableData<K>> T getRepository(Class<A> storableClass) {
        StorableServices services = getStorableServices(storableClass);
        return (T) services.getRepository();
    }

    public Collection<StorableServices> getAllStorableServices() {
        return unmodifiableCollection(storableServices.values());
    }

    public Collection<DomainProcess> getAllDomainProcesses() {
        return unmodifiableCollection(processes.values());
    }

    public Collection<Object> getAllServices() {
        return unmodifiableCollection(services.values());
    }
}
