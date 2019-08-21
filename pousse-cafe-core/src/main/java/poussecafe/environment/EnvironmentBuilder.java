package poussecafe.environment;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import poussecafe.domain.Factory;
import poussecafe.domain.Repository;
import poussecafe.domain.Service;
import poussecafe.exception.PousseCafeException;
import poussecafe.injector.Injector;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageImplementation;
import poussecafe.process.DomainProcess;
import poussecafe.runtime.TransactionRunnerLocator;
import poussecafe.util.ReflectionUtils;

import static java.util.stream.Collectors.toSet;
import static org.slf4j.LoggerFactory.getLogger;

public class EnvironmentBuilder {

    private Environment environment = new Environment();

    public EnvironmentBuilder withBundles(Collection<Bundle> bundles) {
        bundles.forEach(this::withBundle);
        return this;
    }

    public EnvironmentBuilder withBundle(Bundle bundle) {
        withBundleDefinition(bundle.definition());
        withBundleImplementations(bundle);
        return this;
    }

    private EnvironmentBuilder withBundleDefinition(BundleDefinition bundle) {
        bundle.definedAggregates().forEach(this::withAggregateDefinition);
        bundle.definedMessages().forEach(definedMessages::add);
        bundle.definedDomainProcesses().forEach(definedDomainProcesses::add);
        bundle.definedServices().forEach(definedServices::add);
        bundle.definedListeners().forEach(definedListeners::add);
        return this;
    }

    private void withAggregateDefinition(AggregateDefinition definition) {
        Class<?> entityClass = definition.getAggregateRootClass();
        if(aggregateDefinitions.containsKey(entityClass)) {
            if(definition.equals(aggregateDefinitions.get(entityClass))) {
                return;
            } else {
                throw new PousseCafeException("Conflicting definitions found for Entity " + entityClass.getName());
            }
        }
        aggregateDefinitions.put(entityClass, definition);

        if(definition.hasFactory()) {
            environment.entityClassByFactoryOrRepository.put(definition.getFactoryClass(), entityClass);
        }

        if(definition.hasRepository()) {
            environment.entityClassByFactoryOrRepository.put(definition.getRepositoryClass(), entityClass);
        }
    }

    private Map<Class<?>, AggregateDefinition> aggregateDefinitions = new HashMap<>();

    private Set<Class<? extends Message>> definedMessages = new HashSet<>();

    private Set<Class<? extends DomainProcess>> definedDomainProcesses = new HashSet<>();

    private Set<Class<? extends Service>> definedServices = new HashSet<>();

    private Set<MessageListenerDefinition> definedListeners = new HashSet<>();

    private void withBundleImplementations(Bundle bundle) {
        bundle.entityImplementations().stream().forEach(this::withEntityImplementation);
        bundle.messageImplementations().stream().forEach(this::withMessageImplementation);
        bundle.serviceImplementations().stream().forEach(this::withServiceImplementation);
    }

    private void withEntityImplementation(EntityImplementation implementation) {
        Objects.requireNonNull(implementation);
        Class<?> entityClass = implementation.getEntityClass();

        if(!aggregateDefinitions.containsKey(entityClass)
                && implementation.hasDataAccess()) {
            throw new PousseCafeException("Trying to implement an undefined aggregate with root " + entityClass.getName());
        }

        environment.entityImplementations.put(entityClass, implementation);

        if(implementation.hasStorage()) {
            environment.storages.add(implementation.getStorage());
        }
    }

    private void withMessageImplementation(MessageImplementation implementation) {
        Objects.requireNonNull(implementation);
        Class<? extends Message> messageClass = implementation.messageClass();
        if(!definedMessages.contains(messageClass)) {
            throw new PousseCafeException("Trying to implement a message that was not defined: " + messageClass);
        }

        MessageImplementation existingImplementation = environment.messageImplementations.get(messageClass);
        if(existingImplementation != null) {
            if(existingImplementation.messageImplementationClass() != implementation.messageImplementationClass()) {
                throw new PousseCafeException("Conflicting implementations detected for message " + messageClass);
            } else {
                return;
            }
        }

        environment.messageImplementations.put(messageClass, implementation);
        environment.messageClassesPerImplementation.put(implementation.messageImplementationClass(), messageClass);
        environment.messagings.add(implementation.messaging());
        environment.messagingByMessageClass.put(messageClass, implementation.messaging());
    }

    private void withServiceImplementation(ServiceImplementation implementation) {
        Objects.requireNonNull(implementation);
        Class<? extends Service> serviceClass = implementation.serviceClass();

        ServiceImplementation existingImplementation = environment.serviceImplementations.get(serviceClass);
        if(existingImplementation != null) {
            if(existingImplementation.serviceImplementationClass() != implementation.serviceImplementationClass()) {
                throw new PousseCafeException("Conflicting implementations detected for service " + serviceClass);
            } else {
                return;
            }
        }

        environment.serviceImplementations.put(serviceClass, implementation);
    }

    public EnvironmentBuilder injectorBuilder(Injector.Builder injector) {
        this.injector = injector;
        return this;
    }

    private Injector.Builder injector;

    public EnvironmentBuilder transactionRunnerLocator(TransactionRunnerLocator transactionRunnerLocator) {
        this.transactionRunnerLocator = transactionRunnerLocator;
        return this;
    }

    private TransactionRunnerLocator transactionRunnerLocator;

    public Environment build() {
        Objects.requireNonNull(transactionRunnerLocator);
        Objects.requireNonNull(injector);

        checkEnvironment();

        injector.registerInjectableService(environment);
        injector.registerInjectableService(environment.entityFactory());

        registerAggregateServices();
        registerServices();
        registerDomainProcesses();
        registerMessageListeners();

        return environment;
    }

    private void checkEnvironment() {
        if(isAbstract()) {
            Set<Class<?>> abstractEntities = abstractEntities();
            logger.error("{} abstract entities:", abstractEntities.size());
            for(Class<?> abstractEntityClass : abstractEntities) {
                logger.error("- {}", abstractEntityClass.getName());
            }

            Set<Class<? extends Message>> abstractMessages = abstractMessages();
            logger.error("{} abstract messages:", abstractMessages.size());
            for(Class<? extends Message> abstractMessageClass : abstractMessages) {
                logger.error("- {}", abstractMessageClass.getName());
            }

            Set<Class<? extends Service>> abstractServices = abstractServices();
            logger.error("{} abstract services:", abstractServices.size());
            for(Class<? extends Service> abstractServiceClass : abstractServices) {
                logger.error("- {}", abstractServiceClass.getName());
            }

            throw new PousseCafeException("Cannot start meta-application with an abstract environment");
        }

        for(MessageListenerDefinition definition : definedListeners) {
            Class<? extends Message> messageClass = definition.messageClass();
            if(!definedMessages.contains(messageClass)) {
                throw new PousseCafeException("Listener consumes undefined Message " + messageClass.getName());
            }
        }
    }

    private boolean isAbstract() {
        return !abstractEntities().isEmpty() || !abstractMessages().isEmpty() || !abstractServices().isEmpty();
    }

    private Set<Class<?>> abstractEntities() {
        Set<Class<?>> abstractEntities = new HashSet<>();
        for(Class<?> entityClass : aggregateDefinitions.keySet()) {
            if(!environment.entityImplementations.containsKey(entityClass)) {
                abstractEntities.add(entityClass);
            }
        }
        return abstractEntities;
    }

    private Logger logger = getLogger(getClass());

    private Set<Class<? extends Message>> abstractMessages() {
        Set<Class<? extends Message>> abstractMessages = new HashSet<>();
        for(Class<? extends Message> messageClass : definedMessages) {
            if(!environment.messageImplementations.containsKey(messageClass)) {
                abstractMessages.add(messageClass);
            }
        }
        return abstractMessages;
    }

    private Set<Class<? extends Service>> abstractServices() {
        return definedServices.stream()
                .filter(ReflectionUtils::isAbstract)
                .filter(abstractServiceClass -> !environment.serviceImplementations.containsKey(abstractServiceClass))
                .collect(toSet());
    }

    private void registerAggregateServices() {
        AggregateServicesFactory entityServicesFactory = new AggregateServicesFactory(environment);
        for (AggregateDefinition definition : aggregateDefinitions.values()) {
            if(definition.hasFactory() && definition.hasRepository()) {
                registerAggregateServices(entityServicesFactory.buildEntityServices(definition));
            }
        }
    }

    @SuppressWarnings("rawtypes")
    private void registerAggregateServices(AggregateServices entityServices) {
        environment.entityServicesMap.put(entityServices.aggregateRootEntityClass(), entityServices);

        Repository repository = entityServices.repository();
        injector.registerInjectableService(repository);

        Factory factory = entityServices.factory();
        injector.registerInjectableService(factory);
    }

    private void registerServices() {
        for (Class<?> serviceClass : definedServices) {
            if(!ReflectionUtils.isAbstract(serviceClass)) {
                Object service = ReflectionUtils.newInstance(serviceClass);
                injector.registerInjectableService(service);
                environment.registerServiceInstance(serviceClass, service);
            }
        }

        for (ServiceImplementation serviceImplementation : environment.serviceImplementations.values()) {
            Object service = ReflectionUtils.newInstance(serviceImplementation.serviceImplementationClass());
            injector.registerInjectableService(serviceImplementation.serviceClass(), service);
            environment.registerServiceInstance(serviceImplementation.serviceClass(), service);
        }
    }

    private void registerDomainProcesses() {
        for (Class<?> processClass : definedDomainProcesses) {
            DomainProcess process = (DomainProcess) ReflectionUtils.newInstance(processClass);
            injector.registerInjectableService(process);
            environment.registerDomainProcessInstance(process);
        }
    }

    private void registerMessageListeners() {
        MessageListenerFactory messageListenerFactory = new MessageListenerFactory.Builder()
                .environment(environment)
                .transactionRunnerLocator(transactionRunnerLocator)
                .build();
        for(MessageListenerDefinition definition : definedListeners) {
            MessageListener listener = messageListenerFactory.build(definition);
            environment.messageListenerRegistrar.registerListener(listener);
        }
    }
}