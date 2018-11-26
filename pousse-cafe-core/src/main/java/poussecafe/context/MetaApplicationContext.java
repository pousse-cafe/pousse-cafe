package poussecafe.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import poussecafe.domain.EntityImplementation;
import poussecafe.domain.Factory;
import poussecafe.domain.Repository;
import poussecafe.exception.PousseCafeException;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageImplementationConfiguration;
import poussecafe.messaging.MessageListener;
import poussecafe.messaging.MessageListenerRegistry;
import poussecafe.messaging.Messaging;
import poussecafe.messaging.MessagingConnection;
import poussecafe.process.DomainProcess;
import poussecafe.storage.Storage;

import static java.util.Collections.unmodifiableCollection;
import static org.slf4j.LoggerFactory.getLogger;
import static poussecafe.check.Checks.checkThatValue;

public class MetaApplicationContext {

    private Environment environment;

    private ComponentFactory componentFactory;

    private Injector injector;

    private MessageListenerRegistry messageListenerRegistry;

    private TransactionRunnerLocator storageServiceLocator;

    private MessageConsumer messageConsumer;

    private MessageSenderLocator messageSenderLocator;

    private Map<Class<?>, EntityServices> entityServices = new HashMap<>();

    private List<BoundedContext> boundedContexts = new ArrayList<>();

    public MetaApplicationContext() {
        environment = new Environment();

        componentFactory = new ComponentFactory();
        componentFactory.setEnvironment(environment);

        messageListenerRegistry = new MessageListenerRegistry();
        messageListenerRegistry.setEnvironment(environment);

        storageServiceLocator = new TransactionRunnerLocator();
        storageServiceLocator.setEnvironment(environment);

        messageConsumer = new MessageConsumer();

        messageSenderLocator = new MessageSenderLocator(connections);
    }

    public Environment environment() {
        return environment;
    }

    public void addBoundedContext(BoundedContext boundedContext) {
        checkThatValue(boundedContext).notNull();
        boundedContexts.add(boundedContext);
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
        injector.registerInjectableService(storageServiceLocator);
        injector.registerInjectableService(messageListenerRegistry);

        configureContext();
        injector.injectDependencies();
    }

    private void loadBundles() {
        for(BoundedContext appBundle : boundedContexts) {
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
        for(MessageImplementationConfiguration implementation : boundedContext.getMessageImplementations()) {
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
        configureMessageConsumer();
        configureMessageSenderLocator();
        configureMessageEmissionPolicies();
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
            Repository repository = (Repository) componentFactory.newComponent(ComponentSpecification.ofClass(definition.getRepositoryClass()));
            Factory factory = (Factory) componentFactory.newComponent(ComponentSpecification.ofClass(definition.getFactoryClass()));

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
            storage.getMessageSendingPolicy().setMessageSenderLocator(messageSenderLocator);
        }
    }

    private void configureMessageConsumer() {
        injector.addInjectionCandidate(messageConsumer);
    }

    private List<MessagingConnection> connections = new ArrayList<>();

    private void configureMessageSenderLocator() {
        injector.addInjectionCandidate(messageSenderLocator);
        injector.registerInjectableService(messageSenderLocator);
    }

    public synchronized void startMessageHandling() {
        connectMessaging();
        for(MessagingConnection connection : connections) {
            connection.startReceiving();
        }
    }

    private void connectMessaging() {
        for(Messaging messaging : environment.getMessagings()) {
            connections.add(messaging.connect(messageConsumer));
        }
    }

    public synchronized void stopMessageHandling() {
        for(MessagingConnection connection : connections) {
            connection.stopReceiving();
        }
    }

    public Set<MessageListener> getMessageListeners(Class<? extends Message> messageClass) {
        return messageListenerRegistry.getListeners(messageClass);
    }

    public EntityServices getEntityServices(Class<?> entityClass) {
        return entityServices.get(entityClass);
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

    public MessageSenderLocator getMessageSenderLocator() {
        return messageSenderLocator;
    }

    public ComponentFactory getComponentFactory() {
        return componentFactory;
    }

    public List<MessagingConnection> messagingConnections() {
        return Collections.unmodifiableList(connections);
    }
}
