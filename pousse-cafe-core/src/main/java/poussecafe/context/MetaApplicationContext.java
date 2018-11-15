package poussecafe.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.ComponentFactory;
import poussecafe.domain.ComponentSpecification;
import poussecafe.domain.EntityData;
import poussecafe.domain.EntityDefinition;
import poussecafe.domain.Factory;
import poussecafe.domain.MessageImplementation;
import poussecafe.domain.Repository;
import poussecafe.domain.EntityImplementation;
import poussecafe.exception.PousseCafeException;
import poussecafe.messaging.JacksonMessageAdapter;
import poussecafe.messaging.MessageAdapter;
import poussecafe.messaging.MessageListener;
import poussecafe.messaging.MessageListenerRegistry;
import poussecafe.messaging.MessageListenerRoutingKey;
import poussecafe.messaging.MessageReceiver;
import poussecafe.messaging.MessageSender;
import poussecafe.messaging.internal.InternalMessageQueue;
import poussecafe.process.DomainProcess;
import poussecafe.storage.Storage;

import static java.util.Collections.unmodifiableCollection;
import static org.slf4j.LoggerFactory.getLogger;
import static poussecafe.check.Checks.checkThatValue;

public class MetaApplicationContext {

    private Environment environment;

    private ComponentFactory primitiveFactory;

    private Injector injector;

    private MessageListenerRegistry messageListenerRegistry;

    private TransactionRunnerLocator storageServiceLocator;

    private InternalMessageQueue inMemoryMessageQueue;

    private MessageSender messageSender;

    private MessageReceiver messageReceiver;

    private Map<Class<?>, EntityServices> entityServices = new HashMap<>();

    private MessageAdapter messageAdapter;

    private List<BoundedContext> appBundles = new ArrayList<>();

    public MetaApplicationContext() {
        environment = new Environment();

        primitiveFactory = new ComponentFactory();
        primitiveFactory.setEnvironment(environment);

        messageListenerRegistry = new MessageListenerRegistry();

        storageServiceLocator = new TransactionRunnerLocator();
        storageServiceLocator.setEnvironment(environment);

        inMemoryMessageQueue = new InternalMessageQueue();
        messageSender = inMemoryMessageQueue;
        messageReceiver = inMemoryMessageQueue;

        messageAdapter = new JacksonMessageAdapter();
    }

    public Environment environment() {
        return environment;
    }

    public void addBundle(BoundedContext bundle) {
        checkThatValue(bundle).notNull();
        appBundles.add(bundle);
    }

    public synchronized void start() {
        load();
        startMessageHandling();
    }

    public synchronized void load() {
        loadBundles();
        checkEnvironment();

        injector = new Injector();
        injector.registerInjectableService(environment);
        injector.registerInjectableService(MessageAdapter.class, messageAdapter);
        injector.registerInjectableService(storageServiceLocator);
        injector.registerInjectableService(messageListenerRegistry);

        configureContext();
        injector.injectDependencies();
    }

    private void loadBundles() {
        for(BoundedContext appBundle : appBundles) {
            loadBoundedContext(appBundle);
        }
    }

    public void loadBoundedContext(BoundedContext boundedContext) {
        for(EntityDefinition definition : boundedContext.getDefinitions()) {
            environment.defineEntity(definition);
        }
        for(EntityImplementation implementation : boundedContext.getEntityImplementations()) {
            environment.implementEntity(implementation);
        }
        for(MessageImplementation implementation : boundedContext.getMessageImplementations()) {
            environment.implementMessage(implementation);
        }
        for(Class<? extends DomainProcess> processClass : boundedContext.getProcesses()) {
            environment.defineProcess(processClass);
        }
        for(Class<?> serviceClass : boundedContext.getServices()) {
            environment.defineService(serviceClass);
        }
    }

    private void checkEnvironment() {
        if(environment.isAbstract()) {
            Set<Class<?>> abstractEntities = environment.getAbstractEntities();
            logger.error("{} abstract entities:", abstractEntities.size());
            for(Class<?> abstractEntityClass : abstractEntities) {
                logger.error("- {}", abstractEntityClass.getName());
            }
            throw new PousseCafeException("Cannot start meta-application with an abstract environment");
        }
    }

    private Logger logger = getLogger(getClass());

    private void configureContext() {
        configureEntities();
        configureServices();
        configureProcesses();
        configureMessageEmissionPolicies();
        configureMessageReceivers();
    }

    protected void configureEntities() {
        for (Class<?> entityClass : environment.getDefinedEntities()) {
            configureEntity(entityClass);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void configureEntity(Class<?> entityClass) {
        EntityDefinition definition = environment.getEntityDefinition(entityClass);
        if(definition.hasFactory() && definition.hasRepository()) {
            Repository repository = (Repository) primitiveFactory.newComponent(ComponentSpecification.ofClass(definition.getRepositoryClass()));
            Factory factory = (Factory) primitiveFactory.newComponent(ComponentSpecification.ofClass(definition.getFactoryClass()));

            injector.registerInjectableService(repository);
            injector.registerInjectableService(factory);
            injector.addInjectionCandidate(factory);

            entityServices.put(entityClass, new EntityServices(entityClass, repository, factory));
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
        DomainProcessExplorer processExplorer = processExplorer();
        for (Class<?> processClass : environment.getDefinedProcesses()) {
            DomainProcess process = (DomainProcess) newInstance(processClass);
            processExplorer.discoverListeners(process);
            injector.addInjectionCandidate(process);
            injector.registerInjectableService(process);
            processes.put(processClass, process);
        }
    }

    private DomainProcessExplorer processExplorer() {
        DomainProcessExplorer processExplorer = new DomainProcessExplorer();
        processExplorer.setMessageListenerRegistry(messageListenerRegistry);
        return processExplorer;
    }

    private Map<Class<?>, DomainProcess> processes = new HashMap<>();

    protected void configureMessageEmissionPolicies() {
        for (Storage storage : environment.getStorages()) {
            storage.getMessageSendingPolicy().setMessageSender(messageSender);
        }
    }

    private void configureMessageReceivers() {
        injector.addInjectionCandidate(messageReceiver);
    }

    public synchronized void startMessageHandling() {
        messageReceiver.startReceiving();
    }

    public Set<MessageListener> getMessageListeners(MessageListenerRoutingKey key) {
        return messageListenerRegistry.getListeners(key);
    }

    public MessageSender getMessageSender() {
        return messageSender;
    }

    public MessageReceiver getMessageReceiver() {
        return messageReceiver;
    }

    public EntityServices getEntityServices(Class<?> entityClass) {
        return entityServices.get(entityClass);
    }

    public InternalMessageQueue getInMemoryQueue() {
        return inMemoryMessageQueue;
    }

    @SuppressWarnings("unchecked")
    public <T extends DomainProcess> T getDomainProcess(Class<T> processClass) {
        T domainProcess = (T) processes.get(processClass);
        if(domainProcess == null) {
            throw new PousseCafeException("Domain process not found");
        }
        return domainProcess;
    }

    @SuppressWarnings("unchecked")
    public <T extends Repository<A, K, D>, A extends AggregateRoot<K, D>, K, D extends EntityData<K>> T getRepository(Class<A> entityClass) {
        EntityServices services = getEntityServices(entityClass);
        if(services == null) {
            throw new PousseCafeException("Entity services not found");
        }
        return (T) services.getRepository();
    }

    public Collection<EntityServices> getAllEntityServices() {
        return unmodifiableCollection(entityServices.values());
    }

    public Collection<DomainProcess> getAllDomainProcesses() {
        return unmodifiableCollection(processes.values());
    }

    public Collection<Object> getAllServices() {
        return unmodifiableCollection(services.values());
    }

    public synchronized void injectDependencies(Object service) {
        injector.injectDependencies(service);
    }

    public synchronized void registerListeners(Object service) {
        processExplorer().discoverListeners(service);
    }
}
