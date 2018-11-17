package poussecafe.context;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import poussecafe.domain.EntityDefinition;
import poussecafe.domain.EntityImplementation;
import poussecafe.exception.PousseCafeException;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageImplementationConfiguration;
import poussecafe.messaging.Messaging;
import poussecafe.process.DomainProcess;
import poussecafe.storage.Storage;

import static java.util.Collections.unmodifiableSet;
import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class Environment {

    public void defineEntity(EntityDefinition definition) {
        checkThat(value(definition).notNull());
        Class<?> entityClass = definition.getEntityClass();
        if(definitions.containsKey(entityClass)) {
            throw new PousseCafeException("Entity " + entityClass.getName() + " is already defined");
        }
        definitions.put(entityClass, definition);

        if(definition.hasFactory()) {
            entityClassByRelated.put(definition.getFactoryClass(), entityClass);
        }

        if(definition.hasRepository()) {
            entityClassByRelated.put(definition.getRepositoryClass(), entityClass);
        }
    }

    private Map<Class<?>, EntityDefinition> definitions = new HashMap<>();

    private Map<Class<?>, Class<?>> entityClassByRelated = new HashMap<>();

    public void implementEntity(EntityImplementation implementation) {
        checkThat(value(implementation).notNull());
        Class<?> entityClass = implementation.getEntityClass();
        entityImplementations.put(entityClass, implementation);

        if(implementation.hasStorage()) {
            storages.add(implementation.getStorage());
        }
    }

    private Map<Class<?>, EntityImplementation> entityImplementations = new HashMap<>();

    private Set<Storage> storages = new HashSet<>();

    public boolean isAbstract() {
        return !getAbstractEntities().isEmpty();
    }

    public Set<Class<?>> getAbstractEntities() {
        Set<Class<?>> abstractEntities = new HashSet<>();
        for(Class<?> entityClass : definitions.keySet()) {
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

    public EntityDefinition getEntityDefinition(Class<?> entityClass) {
        EntityDefinition definition = definitions.get(entityClass);
        if(definition == null) {
            throw new PousseCafeException("Entity " + entityClass + " is not defined");
        }
        return definition;
    }

    public Set<Class<?>> getDefinedEntities() {
        return unmodifiableSet(definitions.keySet());
    }

    public void defineService(Class<?> serviceClass) {
        checkThat(value(serviceClass).notNull());
        services.add(serviceClass);
    }

    private Set<Class<?>> services = new HashSet<>();

    public Set<Class<?>> getDefinedServices() {
        return unmodifiableSet(services);
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

    public void implementMessage(MessageImplementationConfiguration implementation) {
        checkThat(value(implementation).notNull());
        Class<? extends Message> messageClass = implementation.getMessageClass();
        messageImplementations.put(messageClass, implementation);
        messageClassesPerImplementation.put(implementation.getMessageImplementationClass(), messageClass);
        messagings.add(implementation.getMessaging());
        messagingByMessageClass.put(messageClass, implementation.getMessaging());
    }

    private Map<Class<?>, MessageImplementationConfiguration> messageImplementations = new HashMap<>();

    private Map<Class<? extends Message>, Class<? extends Message>> messageClassesPerImplementation = new HashMap<>();

    private Map<Class<?>, Messaging> messagingByMessageClass = new HashMap<>();

    private Set<Messaging> messagings = new HashSet<>();

    public Set<Messaging> getMessagings() {
        return unmodifiableSet(messagings);
    }

    public Class<?> getMessageImplementationClass(Class<?> messageClass) {
        MessageImplementationConfiguration implementation = getMessageImplementation(messageClass);
        return implementation.getMessageImplementationClass();
    }

    private MessageImplementationConfiguration getMessageImplementation(Class<?> messageClass) {
        MessageImplementationConfiguration implementation = messageImplementations.get(messageClass);
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
}
