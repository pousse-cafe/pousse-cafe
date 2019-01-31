package poussecafe.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import poussecafe.domain.AggregateDefinition;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.ComponentFactory;
import poussecafe.domain.EntityData;
import poussecafe.domain.Factory;
import poussecafe.domain.Repository;
import poussecafe.domain.Service;
import poussecafe.exception.PousseCafeException;
import poussecafe.injector.Injector;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageListener;
import poussecafe.messaging.MessageListenerRegistry;
import poussecafe.messaging.Messaging;
import poussecafe.messaging.MessagingConnection;
import poussecafe.process.DomainProcess;
import poussecafe.storage.Storage;
import poussecafe.util.ReflectionUtils;

import static java.util.Collections.unmodifiableCollection;
import static org.slf4j.LoggerFactory.getLogger;

public class MetaApplicationContext {

    private Configuration configuration;

    private Environment environment;

    private ComponentFactory componentFactory;

    private Injector injector;

    private MessageListenerRegistry messageListenerRegistry;

    private TransactionRunnerLocator storageServiceLocator;

    private MessageConsumer messageConsumer;

    private MessageSenderLocator messageSenderLocator;

    private Map<Class<?>, EntityServices> entityServices = new HashMap<>();

    private Set<BoundedContext> boundedContexts = new HashSet<>();

    public MetaApplicationContext() {
        configuration = new Configuration();
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

    public Configuration configuration() {
        return configuration;
    }

    public Environment environment() {
        return environment;
    }

    public void addBoundedContext(BoundedContext boundedContext) {
        Objects.requireNonNull(boundedContext);
        boundedContexts.add(boundedContext);
    }

    public synchronized void addConfiguration(String key, Object value) {
        if(started) {
            throw new PousseCafeException("Cannot add configuration entries after meta-application context has been started");
        }
        configuration.add(key, value);
    }

    public synchronized void start() {
        if(started) {
            return;
        }
        started = true;
        load();
        startMessageHandling();
    }

    private boolean started;

    public synchronized void load() {
        loadBoundedContextDefinitions();
        loadBoundedContextImplementations();
        checkEnvironment();

        injector = new Injector();
        injector.registerInjectableService(configuration);
        injector.registerInjectableService(environment);
        injector.registerInjectableService(storageServiceLocator);
        injector.registerInjectableService(messageListenerRegistry);
        injector.registerInjectableService(componentFactory);

        configureContext();
        injector.injectDependencies();
    }

    private void loadBoundedContextDefinitions() {
        for(BoundedContext boundedContext : boundedContexts) {
            loadBoundedContextDefinition(boundedContext.definition());
        }
    }

    private void loadBoundedContextDefinition(BoundedContextDefinition boundedContext) {
        for(AggregateDefinition entityDefinition : boundedContext.getEntityDefinitions()) {
            environment.defineAggregate(entityDefinition);
        }
        for(Class<? extends Message> messageClass : boundedContext.getMessages()) {
            environment.defineMessage(messageClass);
        }
        for(Class<? extends DomainProcess> processClass : boundedContext.getProcesses()) {
            environment.defineProcess(processClass);
        }
        for(Class<? extends Service> serviceClass : boundedContext.getServices()) {
            environment.defineService(serviceClass);
        }
    }

    private void loadBoundedContextImplementations() {
        for(BoundedContext boundedContext : boundedContexts) {
            loadBoundedContextImplementations(boundedContext);
        }
    }

    private void loadBoundedContextImplementations(BoundedContext boundedContext) {
        loadStorageImplementations(boundedContext);
        loadMessagingImplementations(boundedContext);
        loadServiceImplementations(boundedContext);
    }

    private void loadStorageImplementations(BoundedContext boundedContext) {
        boundedContext.storageImplementations().stream().forEach(environment::implementEntity);
    }

    private void loadMessagingImplementations(BoundedContext boundedContext) {
        boundedContext.messagingImplementations().stream().forEach(environment::implementMessage);
    }

    private void loadServiceImplementations(BoundedContext boundedContext) {
        boundedContext.serviceImplementations().stream().forEach(environment::implementService);
    }

    private void checkEnvironment() {
        if(environment.isAbstract()) {
            Set<Class<?>> abstractEntities = environment.getAbstractEntities();
            logger.error("{} abstract entities:", abstractEntities.size());
            for(Class<?> abstractEntityClass : abstractEntities) {
                logger.error("- {}", abstractEntityClass.getName());
            }

            Set<Class<? extends Message>> abstractMessages = environment.getAbstractMessages();
            logger.error("{} abstract messages:", abstractMessages.size());
            for(Class<? extends Message> abstractMessageClass : abstractMessages) {
                logger.error("- {}", abstractMessageClass.getName());
            }

            Set<Class<? extends Service>> abstractServices = environment.abstractServices();
            logger.error("{} abstract services:", abstractServices.size());
            for(Class<? extends Service> abstractServiceClass : abstractServices) {
                logger.error("- {}", abstractServiceClass.getName());
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
        AggregateDefinition definition = environment.getEntityDefinition(entityClass);
        if(definition.hasFactory() && definition.hasRepository()) {
            Repository repository = componentFactory.newRepository(definition.getRepositoryClass());
            Factory factory = componentFactory.newFactory(definition.getFactoryClass());

            injector.registerInjectableService(repository);
            injector.addInjectionCandidate(repository);

            injector.registerInjectableService(factory);
            injector.addInjectionCandidate(factory);

            entityServices.put(entityClass, new EntityServices(entityClass, repository, factory));
        }
    }

    protected void configureServices() {
        for (Class<?> serviceClass : environment.getDefinedServices()) {
            if(!ReflectionUtils.isAbstract(serviceClass)) {
                Object service = newInstance(serviceClass);
                injector.registerInjectableService(service);
                injector.addInjectionCandidate(service);
                services.put(serviceClass, service);
            }
        }

        for (ServiceImplementation serviceImplementation : environment.serviceImplementations()) {
            Object service = newInstance(serviceImplementation.serviceImplementationClass());
            injector.registerInjectableService(serviceImplementation.serviceClass(), service);
            injector.addInjectionCandidate(service);
            if(services.put(serviceImplementation.serviceClass(), service) != null) {
                throw new PousseCafeException("Service was already implemented " + serviceImplementation.serviceClass());
            }
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
        logger.info("Starting message handling...");
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
