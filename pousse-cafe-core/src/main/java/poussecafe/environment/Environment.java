package poussecafe.environment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import poussecafe.domain.AggregateFactory;
import poussecafe.domain.AggregateRepository;
import poussecafe.domain.Service;
import poussecafe.exception.PousseCafeException;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageImplementation;
import poussecafe.messaging.Messaging;
import poussecafe.process.ExplicitDomainProcess;
import poussecafe.processing.ListenersSet;
import poussecafe.runtime.MessageValidator;
import poussecafe.runtime.NoOpMessageValidator;
import poussecafe.storage.Storage;

import static java.util.Collections.unmodifiableCollection;
import static java.util.Collections.unmodifiableSet;
import static java.util.stream.Collectors.toList;

public class Environment {

    Environment() {
        messageFactory = new MessageFactory(this);
        messageListenerRegistrar = new MessageListenerRegistrar();
        messageListenerRegistrar.setEnvironment(this);
    }

    MessageListenerRegistrar messageListenerRegistrar;

    void initEntityFactory(boolean messageValidation) {
        if(messageValidation) {
            messageValidator = new EnvironmentMessageValidator(this);
        } else {
            messageValidator = NoOpMessageValidator.instance();
        }
        entityFactory = new EntityFactory.Builder()
                .environment(this)
                .messageFactory(messageFactory)
                .messageValidator(messageValidator)
                .build();
    }

    private MessageValidator messageValidator;

    public MessageValidator messageValidator() {
        return messageValidator;
    }

    private EntityFactory entityFactory;

    public EntityFactory entityFactory() {
        return entityFactory;
    }

    private MessageFactory messageFactory;

    public MessageFactory messageFactory() {
        return messageFactory;
    }

    Map<Class<?>, EntityImplementation> entityImplementations = new HashMap<>();

    Map<String, Set<EntityImplementation>> entityImplementationsBySimpleName = new HashMap<>();

    Map<String, EntityImplementation> entityImplementationsByQualifiedName = new HashMap<>();

    Set<Storage> storages = new HashSet<>();

    Storage storageOfEntity(Class<?> entityClass) {
        EntityImplementation implementation = entityImplementationOrElseThrow(entityClass);
        return implementation.getStorage();
    }

    public EntityImplementation entityImplementationOrElseThrow(Class<?> entityClass) {
        return entityImplementation(entityClass).orElseThrow(
                () -> new PousseCafeException("Entity " + entityClass.getName() + " is not implemented"));
    }

    public Optional<EntityImplementation> entityImplementation(Class<?> entityClass) {
        return Optional.ofNullable(entityImplementations.get(entityClass));
    }

    public Optional<EntityImplementation> entityImplementationByName(String name) {
        Set<EntityImplementation> implementations = entityImplementationsBySimpleName.get(name);
        if(implementations == null) {
            var implementation = entityImplementationsByQualifiedName.get(name);
            if(implementation == null) {
                try {
                    Class<?> entityClass = Class.forName(name);
                    return entityImplementation(entityClass);
                } catch (ClassNotFoundException e) {
                    return Optional.empty();
                }
            } else {
                return Optional.of(implementation);
            }
        } else if(implementations.size() != 1) {
            throw new PousseCafeException("Simple name is ambiguous, please use a qualified name");
        } else {
            return Optional.of(implementations.iterator().next());
        }
    }

    Supplier<Object> entityDataFactory(Class<?> entityClass) {
        EntityImplementation implementation = entityImplementationOrElseThrow(entityClass);
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
        EntityImplementation implementation = entityImplementationOrElseThrow(entityClass);
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

    public Class<? extends Message> messageImplementationClass(Class<?> messageClass) {
        MessageImplementation implementation = messageImplementation(messageClass);
        return implementation.messageImplementationClass();
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

    void registerDomainProcessInstance(ExplicitDomainProcess domainProcessInstance) {
        processInstances.put(domainProcessInstance.getClass(), domainProcessInstance);
    }

    private Map<Class<?>, ExplicitDomainProcess> processInstances = new HashMap<>();

    public Optional<AggregateServices> aggregateServicesOf(Class<?> aggregateRootClass) {
        return Optional.ofNullable(entityServicesMap.get(aggregateRootClass));
    }

    @SuppressWarnings("unchecked")
    public <T extends ExplicitDomainProcess> Optional<T> domainProcess(Class<T> processClass) {
        return Optional.ofNullable(processInstances.get(processClass)).map(instance -> (T) instance);
    }

    public Collection<AggregateServices> aggregateServices() {
        return unmodifiableCollection(entityServicesMap.values());
    }

    public Collection<ExplicitDomainProcess> domainProcesses() {
        return unmodifiableCollection(processInstances.values());
    }

    public Collection<Object> services() {
        return unmodifiableCollection(serviceInstances.values());
    }

    public <S> Optional<S> service(Class<S> serviceClass) {
        @SuppressWarnings("unchecked")
        S service = (S) serviceInstances.get(serviceClass);
        return Optional.ofNullable(service);
    }

    public void registerMessageListener(MessageListener listener) {
        messageListenerRegistrar.registerListener(listener);
    }

    public Set<MessageListener> messageListenersOf(Class<? extends Message> messageClass) {
        return messageListenerRegistrar.getListeners(messageClass);
    }

    public <F extends AggregateFactory<?, ?, ?>> Optional<F> factoryOf(Class<?> aggregateRootClass) {
        return aggregateServicesOf(aggregateRootClass).map(AggregateServices::factory);
    }

    public <F extends AggregateFactory<?, ?, ?>> Optional<F> factory(Class<?> factoryClass) {
        return factoryOf(entityOfFactoryOrRepository(factoryClass));
    }

    public <R extends AggregateRepository<?, ?, ?>> Optional<R> repositoryOf(Class<?> aggregateRootClass) {
        return aggregateServicesOf(aggregateRootClass).map(AggregateServices::repository);
    }

    public <R extends AggregateRepository<?, ?, ?>> Optional<R> repository(Class<?> repositoryClass) {
        return repositoryOf(entityOfFactoryOrRepository(repositoryClass));
    }

    public Collection<Object> injectionCandidates() {
        List<Object> candidates = new ArrayList<>();
        for(AggregateServices aggregateServices : entityServicesMap.values()) {
            candidates.add(aggregateServices.factory());
            candidates.add(aggregateServices.repository());
        }
        candidates.addAll(serviceInstances.values());
        candidates.addAll(processInstances.values());
        candidates.addAll(messageListenerRegistrar.allListeners().stream()
                .filter(listener -> listener.runner().isPresent())
                .map(listener -> listener.runner().get())
                .collect(toList()));
        return candidates;
    }

    public boolean messageImplemented(Class<? extends Message> messageClass) {
        return messageImplementations.containsKey(messageClass);
    }

    public ListenersSet messageListenersSet() {
        return messageListenerRegistrar.listenersSet();
    }
}
