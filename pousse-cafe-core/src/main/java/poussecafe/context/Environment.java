package poussecafe.context;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import poussecafe.domain.AggregateDefinition;
import poussecafe.domain.EntityFactory;
import poussecafe.domain.EntityImplementation;
import poussecafe.domain.Factory;
import poussecafe.domain.MessageFactory;
import poussecafe.domain.Repository;
import poussecafe.domain.Service;
import poussecafe.exception.PousseCafeException;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageImplementation;
import poussecafe.messaging.MessageListener;
import poussecafe.messaging.MessageListenerDefinition;
import poussecafe.messaging.MessageListenerRegistry;
import poussecafe.messaging.Messaging;
import poussecafe.process.DomainProcess;
import poussecafe.storage.Storage;
import poussecafe.util.ReflectionUtils;

import static java.util.Collections.unmodifiableCollection;
import static java.util.Collections.unmodifiableSet;
import static java.util.stream.Collectors.toSet;
import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class Environment {

    public Environment() {
        messageListenerRegistry = new MessageListenerRegistry();
        messageListenerRegistry.setEnvironment(this);

        messageFactory = new MessageFactory(this);
        entityFactory = new EntityFactory.Builder()
                .environment(this)
                .messageFactory(messageFactory)
                .build();
    }

    private MessageListenerRegistry messageListenerRegistry;

    public MessageListenerRegistry messageListenerRegistry() {
        return messageListenerRegistry;
    }

    private EntityFactory entityFactory;

    public EntityFactory entityFactory() {
        return entityFactory;
    }

    private MessageFactory messageFactory;

    public MessageFactory messageFactory() {
        return messageFactory;
    }

    public void defineAggregate(AggregateDefinition definition) {
        checkThat(value(definition).notNull());
        Class<?> entityClass = definition.getEntityClass();
        if(aggregateDefinitions.containsKey(entityClass)) {
            if(definition.equals(aggregateDefinitions.get(entityClass))) {
                return;
            } else {
                throw new PousseCafeException("Conflicting definitions found for Entity " + entityClass.getName());
            }
        }
        aggregateDefinitions.put(entityClass, definition);

        if(definition.hasFactory()) {
            entityClassByRelated.put(definition.getFactoryClass(), entityClass);
        }

        if(definition.hasRepository()) {
            entityClassByRelated.put(definition.getRepositoryClass(), entityClass);
        }
    }

    private Map<Class<?>, AggregateDefinition> aggregateDefinitions = new HashMap<>();

    private Map<Class<?>, Class<?>> entityClassByRelated = new HashMap<>();

    public void implementEntity(EntityImplementation implementation) {
        checkThat(value(implementation).notNull());
        Class<?> entityClass = implementation.getEntityClass();

        if(!aggregateDefinitions.containsKey(entityClass)
                && implementation.hasDataAccess()) {
            throw new PousseCafeException("Trying to implement undefined aggregate with root " + entityClass.getName());
        }

        entityImplementations.put(entityClass, implementation);

        if(implementation.hasStorage()) {
            storages.add(implementation.getStorage());
        }
    }

    private Map<Class<?>, EntityImplementation> entityImplementations = new HashMap<>();

    private Set<Storage> storages = new HashSet<>();

    public boolean isAbstract() {
        return !getAbstractEntities().isEmpty() || !getAbstractMessages().isEmpty() || !abstractServices().isEmpty();
    }

    public Set<Class<?>> getAbstractEntities() {
        Set<Class<?>> abstractEntities = new HashSet<>();
        for(Class<?> entityClass : aggregateDefinitions.keySet()) {
            if(!entityImplementations.containsKey(entityClass)) {
                abstractEntities.add(entityClass);
            }
        }
        return abstractEntities;
    }

    public Storage getStorage(Class<?> entityClass) {
        EntityImplementation implementation = getEntityImplementation(entityClass);
        return implementation.getStorage();
    }

    public EntityImplementation getEntityImplementation(Class<?> entityClass) {
        EntityImplementation implementation = entityImplementations.get(entityClass);
        if(implementation == null) {
            throw new PousseCafeException("Entity " + entityClass.getName() + " is not implemented");
        }
        return implementation;
    }

    public Supplier<Object> getEntityDataFactory(Class<?> entityClass) {
        EntityImplementation implementation = getEntityImplementation(entityClass);
        return implementation.getDataFactory();
    }

    public Class<?> getEntityClass(Class<?> relatedClass) {
        Class<?> entityClass = entityClassByRelated.get(relatedClass);
        if(entityClass == null) {
            throw new PousseCafeException("No entity for related class " + relatedClass.getName());
        }
        return entityClass;
    }

    public Supplier<Object> getEntityDataAccessFactory(Class<?> entityClass) {
        EntityImplementation implementation = getEntityImplementation(entityClass);
        if(!implementation.hasDataAccess()) {
            throw new PousseCafeException("Entity " + entityClass + " has no data access");
        }
        return implementation.getDataAccessFactory();
    }

    public AggregateDefinition getEntityDefinition(Class<?> entityClass) {
        AggregateDefinition definition = aggregateDefinitions.get(entityClass);
        if(definition == null) {
            throw new PousseCafeException("Entity " + entityClass + " is not defined");
        }
        return definition;
    }

    public Set<Class<?>> getDefinedEntities() {
        return unmodifiableSet(aggregateDefinitions.keySet());
    }

    public void defineService(Class<? extends Service> serviceClass) {
        checkThat(value(serviceClass).notNull());
        serviceDefinitions.add(serviceClass);
    }

    private Set<Class<? extends Service>> serviceDefinitions = new HashSet<>();

    public Set<Class<? extends Service>> getDefinedServices() {
        return unmodifiableSet(serviceDefinitions);
    }

    public void defineProcess(Class<? extends DomainProcess> processClass) {
        checkThat(value(processClass).notNull());
        processes.add(processClass);
    }

    private Set<Class<? extends DomainProcess>> processes = new HashSet<>();

    public Set<Class<? extends DomainProcess>> getDefinedProcesses() {
        return unmodifiableSet(processes);
    }

    public Set<Storage> getStorages() {
        return unmodifiableSet(storages);
    }

    public boolean hasStorageImplementation(Class<?> primitiveClass) {
        return entityImplementations.containsKey(primitiveClass);
    }

    public void defineMessage(Class<? extends Message> messageClass) {
        messageDefinitions.add(messageClass);
    }

    private Set<Class<? extends Message>> messageDefinitions = new HashSet<>();

    public Set<Class<? extends Message>> getDefinedMessages() {
        return Collections.unmodifiableSet(messageDefinitions);
    }

    public void implementMessage(MessageImplementation implementation) {
        checkThat(value(implementation).notNull());
        Class<? extends Message> messageClass = implementation.getMessageClass();
        if(!messageDefinitions.contains(messageClass)) {
            throw new PousseCafeException("Trying to implement a message that was not defined: " + messageClass);
        }
        MessageImplementation existingImplementation = messageImplementations.get(messageClass);
        if(existingImplementation != null) {
            if(existingImplementation.getMessageImplementationClass() != implementation.getMessageImplementationClass()) {
                throw new PousseCafeException("Conflicting implementations detected for message " + messageClass);
            } else {
                return;
            }
        }

        messageImplementations.put(messageClass, implementation);
        messageClassesPerImplementation.put(implementation.getMessageImplementationClass(), messageClass);
        messagings.add(implementation.getMessaging());
        messagingByMessageClass.put(messageClass, implementation.getMessaging());
    }

    private Map<Class<?>, MessageImplementation> messageImplementations = new HashMap<>();

    private Map<Class<? extends Message>, Class<? extends Message>> messageClassesPerImplementation = new HashMap<>();

    private Map<Class<?>, Messaging> messagingByMessageClass = new HashMap<>();

    private Set<Messaging> messagings = new HashSet<>();

    public Set<Messaging> getMessagings() {
        return unmodifiableSet(messagings);
    }

    public Set<Class<? extends Message>> getAbstractMessages() {
        Set<Class<? extends Message>> abstractMessages = new HashSet<>();
        for(Class<? extends Message> messageClass : messageDefinitions) {
            if(!messageImplementations.containsKey(messageClass)) {
                abstractMessages.add(messageClass);
            }
        }
        return abstractMessages;
    }

    public Class<?> getMessageImplementationClass(Class<?> messageClass) {
        MessageImplementation implementation = getMessageImplementation(messageClass);
        return implementation.getMessageImplementationClass();
    }

    private MessageImplementation getMessageImplementation(Class<?> messageClass) {
        MessageImplementation implementation = messageImplementations.get(messageClass);
        if(implementation == null) {
            throw new PousseCafeException("Message " + messageClass.getName() + " is not implemented");
        }
        return implementation;
    }

    public Class<? extends Message> getMessageClass(Class<? extends Message> messageClassOrImplementation) {
        Class<? extends Message> messageClass = messageClassesPerImplementation.get(messageClassOrImplementation);
        if(messageClass == null) {
            return messageClassOrImplementation;
        } else {
            return messageClass;
        }
    }

    public Messaging getMessaging(Class<? extends Message> messageClass) {
        return messagingByMessageClass.get(messageClass);
    }

    public void implementService(ServiceImplementation implementation) {
        Objects.requireNonNull(implementation);
        Class<? extends Service> serviceClass = implementation.serviceClass();

        ServiceImplementation existingImplementation = serviceImplementations.get(serviceClass);
        if(existingImplementation != null) {
            if(existingImplementation.serviceImplementationClass() != implementation.serviceImplementationClass()) {
                throw new PousseCafeException("Conflicting implementations detected for service " + serviceClass);
            } else {
                return;
            }
        }

        serviceImplementations.put(serviceClass, implementation);
    }

    private Map<Class<? extends Service>, ServiceImplementation> serviceImplementations = new HashMap<>();

    public Collection<ServiceImplementation> serviceImplementations() {
        return Collections.unmodifiableCollection(serviceImplementations.values());
    }

    public Set<Class<? extends Service>> abstractServices() {
        return serviceDefinitions.stream()
                .filter(ReflectionUtils::isAbstract)
                .filter(abstractServiceClass -> !serviceImplementations.containsKey(abstractServiceClass))
                .collect(toSet());
    }

    public void defineListener(MessageListenerDefinition definition) {
        Objects.requireNonNull(definition);
        listenerDefinitions.add(definition);
    }

    public Set<MessageListenerDefinition> definedListeners() {
        return unmodifiableSet(listenerDefinitions);
    }

    private Set<MessageListenerDefinition> listenerDefinitions = new HashSet<>();

    public void registerEntityServices(EntityServices entityServices) {
        Objects.requireNonNull(entityServices);
        entityServicesMap.put(entityServices.getEntityClass(), entityServices);
    }

    private Map<Class<?>, EntityServices> entityServicesMap = new HashMap<>();

    public void registerServiceInstance(Class<?> serviceClass,
            Object serviceInstance) {
        if(serviceInstances.containsKey(serviceClass)) {
            throw new PousseCafeException("Service was already implemented " + serviceClass);
        }
        serviceInstances.put(serviceClass, serviceInstance);
    }

    private Map<Class<?>, Object> serviceInstances = new HashMap<>();

    public void registerDomainProcessInstance(DomainProcess domainProcessInstance) {
        processInstances.put(domainProcessInstance.getClass(), domainProcessInstance);
    }

    private Map<Class<?>, DomainProcess> processInstances = new HashMap<>();

    public EntityServices getEntityServices(Class<?> entityClass) {
        return entityServicesMap.get(entityClass);
    }

    public DomainProcess getDomainProcessInstance(Class<? extends DomainProcess> processClass) {
        return processInstances.get(processClass);
    }

    public Collection<EntityServices> getAllEntityServices() {
        return unmodifiableCollection(entityServicesMap.values());
    }

    public Collection<DomainProcess> getAllDomainProcesses() {
        return unmodifiableCollection(processInstances.values());
    }

    public Collection<Object> getAllServiceInstances() {
        return unmodifiableCollection(serviceInstances.values());
    }

    @SuppressWarnings("rawtypes")
    public Factory getFactoryInstance(Class factoryClass) {
        return getEntityServices(getEntityClass(factoryClass)).getFactory();
    }

    @SuppressWarnings("rawtypes")
    public Repository getRepositoryInstance(Class repositoryClass) {
        return getEntityServices(getEntityClass(repositoryClass)).getRepository();
    }

    public void registerListener(MessageListener listener) {
        messageListenerRegistry.registerListener(listener);
    }

    public Set<MessageListener> getListeners(Class<? extends Message> messageClass) {
        return messageListenerRegistry.getListeners(messageClass);
    }

    public Collection<AggregateDefinition> entityDefinitions() {
        return unmodifiableCollection(aggregateDefinitions.values());
    }
}
