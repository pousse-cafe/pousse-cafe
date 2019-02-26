package poussecafe.environment;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import poussecafe.domain.Factory;
import poussecafe.domain.Repository;
import poussecafe.domain.Service;
import poussecafe.exception.PousseCafeException;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageImplementation;
import poussecafe.messaging.MessageListener;
import poussecafe.messaging.MessageListenerRegistry;
import poussecafe.messaging.Messaging;
import poussecafe.process.DomainProcess;
import poussecafe.runtime.AggregateServices;
import poussecafe.storage.Storage;

import static java.util.Collections.unmodifiableCollection;
import static java.util.Collections.unmodifiableSet;

public class Environment {

    public Environment() {
        messageFactory = new MessageFactory(this);
        entityFactory = new EntityFactory.Builder()
                .environment(this)
                .messageFactory(messageFactory)
                .build();
        messageListenerRegistry = new MessageListenerRegistry();
        messageListenerRegistry.setEnvironment(this);
    }

    MessageListenerRegistry messageListenerRegistry;

    private EntityFactory entityFactory;

    public EntityFactory entityFactory() {
        return entityFactory;
    }

    private MessageFactory messageFactory;

    public MessageFactory messageFactory() {
        return messageFactory;
    }

    Map<Class<?>, EntityImplementation> entityImplementations = new HashMap<>();

    Set<Storage> storages = new HashSet<>();

    Storage storageOfEntity(Class<?> entityClass) {
        EntityImplementation implementation = entityImplementation(entityClass);
        return implementation.getStorage();
    }

    public EntityImplementation entityImplementation(Class<?> entityClass) {
        EntityImplementation implementation = entityImplementations.get(entityClass);
        if(implementation == null) {
            throw new PousseCafeException("Entity " + entityClass.getName() + " is not implemented");
        }
        return implementation;
    }

    Supplier<Object> entityDataFactory(Class<?> entityClass) {
        EntityImplementation implementation = entityImplementation(entityClass);
        return implementation.getDataFactory();
    }

    Class<?> entityOfFactoryOrRepository(Class<?> factoryOrRepositoryClass) {
        Class<?> entityClass = entityClassByFactoryOrRepository.get(factoryOrRepositoryClass);
        if(entityClass == null) {
            throw new PousseCafeException("No entity for factory or repository class " + factoryOrRepositoryClass.getName());
        }
        return entityClass;
    }

    Map<Class<?>, Class<?>> entityClassByFactoryOrRepository = new HashMap<>();

    Supplier<Object> entityDataAccessFactory(Class<?> entityClass) {
        EntityImplementation implementation = entityImplementation(entityClass);
        if(!implementation.hasDataAccess()) {
            throw new PousseCafeException("Entity " + entityClass + " has no data access");
        }
        return implementation.dataAccessFactory();
    }

    public Set<Storage> storages() {
        return unmodifiableSet(storages);
    }

    Map<Class<?>, MessageImplementation> messageImplementations = new HashMap<>();

    Map<Class<? extends Message>, Class<? extends Message>> messageClassesPerImplementation = new HashMap<>();

    Map<Class<?>, Messaging> messagingByMessageClass = new HashMap<>();

    Set<Messaging> messagings = new HashSet<>();

    public Set<Messaging> messagings() {
        return unmodifiableSet(messagings);
    }

    Class<?> messageImplementationClass(Class<?> messageClass) {
        MessageImplementation implementation = messageImplementation(messageClass);
        return implementation.getMessageImplementationClass();
    }

    private MessageImplementation messageImplementation(Class<?> messageClass) {
        MessageImplementation implementation = messageImplementations.get(messageClass);
        if(implementation == null) {
            throw new PousseCafeException("Message " + messageClass.getName() + " is not implemented");
        }
        return implementation;
    }

    public Class<? extends Message> definedMessageClass(Class<? extends Message> messageClassOrImplementation) {
        Class<? extends Message> messageClass = messageClassesPerImplementation.get(messageClassOrImplementation);
        if(messageClass == null) {
            return messageClassOrImplementation;
        } else {
            return messageClass;
        }
    }

    public Messaging messagingOf(Class<? extends Message> messageClass) {
        return messagingByMessageClass.get(messageClass);
    }

    Map<Class<? extends Service>, ServiceImplementation> serviceImplementations = new HashMap<>();

    Map<Class<?>, AggregateServices> entityServicesMap = new HashMap<>();

    void registerServiceInstance(Class<?> serviceClass,
            Object serviceInstance) {
        if(serviceInstances.containsKey(serviceClass)) {
            throw new PousseCafeException("Service was already implemented " + serviceClass);
        }
        serviceInstances.put(serviceClass, serviceInstance);
    }

    private Map<Class<?>, Object> serviceInstances = new HashMap<>();

    void registerDomainProcessInstance(DomainProcess domainProcessInstance) {
        processInstances.put(domainProcessInstance.getClass(), domainProcessInstance);
    }

    private Map<Class<?>, DomainProcess> processInstances = new HashMap<>();

    public Optional<AggregateServices> aggregateServicesOf(Class<?> aggregateRootClass) {
        return Optional.ofNullable(entityServicesMap.get(aggregateRootClass));
    }

    @SuppressWarnings("unchecked")
    public <T extends DomainProcess> Optional<T> domainProcess(Class<T> processClass) {
        return Optional.ofNullable(processInstances.get(processClass)).map(instance -> (T) instance);
    }

    public Collection<AggregateServices> aggregateServices() {
        return unmodifiableCollection(entityServicesMap.values());
    }

    public Collection<DomainProcess> domainProcesses() {
        return unmodifiableCollection(processInstances.values());
    }

    public Collection<Object> services() {
        return unmodifiableCollection(serviceInstances.values());
    }

    public void registerMessageListener(MessageListener listener) {
        messageListenerRegistry.registerListener(listener);
    }

    public Set<MessageListener> messageListenersOf(Class<? extends Message> messageClass) {
        return messageListenerRegistry.getListeners(messageClass);
    }

    @SuppressWarnings("unchecked")
    public <F extends Factory<?, ?, ?>> Optional<F> factoryOf(Class<?> aggregateRootClass) {
        return (Optional<F>) aggregateServicesOf(aggregateRootClass).map(AggregateServices::getFactory);
    }

    public <F extends Factory<?, ?, ?>> Optional<F> factory(Class<?> factoryClass) {
        return factoryOf(entityOfFactoryOrRepository(factoryClass));
    }

    @SuppressWarnings("unchecked")
    public <R extends Repository<?, ?, ?>> Optional<R> repositoryOf(Class<?> aggregateRootClass) {
        return (Optional<R>) aggregateServicesOf(aggregateRootClass).map(AggregateServices::getRepository);
    }

    public <R extends Repository<?, ?, ?>> Optional<R> repository(Class<?> repositoryClass) {
        return repositoryOf(entityOfFactoryOrRepository(repositoryClass));
    }
}
