package poussecafe.discovery;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.domain.AggregateFactory;
import poussecafe.domain.AggregateRepository;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.DomainEvent;
import poussecafe.domain.EntityAttributes;
import poussecafe.domain.EntityDataAccess;
import poussecafe.domain.Module;
import poussecafe.domain.Service;
import poussecafe.environment.AggregateDefinition;
import poussecafe.environment.MessageListenerDefinition;
import poussecafe.exception.PousseCafeException;
import poussecafe.messaging.Message;
import poussecafe.messaging.Messaging;
import poussecafe.process.DomainProcess;
import poussecafe.runtime.Command;
import poussecafe.storage.Storage;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

class ClassPathExplorer {

    ClassPathExplorer(Collection<String> basePackages) {
        var completedBasePackages = withPousseCafeBasePackages(basePackages);

        ConfigurationBuilder reflectionsConfigurationBuilder = new ConfigurationBuilder();
        FilterBuilder filter = new FilterBuilder();
        ClassLoader[] classLoaders = null;
        for(String basePackage : completedBasePackages) {
            validOrThrow(basePackage);
            String packageRootPrefix = basePackage + ".";
            filter.includePackage(packageRootPrefix);
            reflectionsConfigurationBuilder.addUrls(ClasspathHelper.forPackage(packageRootPrefix, classLoaders));
        }
        filter.exclude(".*\\.java");

        reflectionsConfigurationBuilder.filterInputsBy(filter);
        reflectionsConfigurationBuilder.setExpandSuperTypes(false);
        reflections = new Reflections(reflectionsConfigurationBuilder);
    }

    private void validOrThrow(String basePackage) {
        if(!basePackage.matches(VALID_PACKAGE_NAME_REGEX)) {
            throw new IllegalArgumentException("Invalid package name " + basePackage);
        }
    }

    private static final String VALID_PACKAGE_NAME_REGEX = "^[a-z][a-z0-9_]*(\\.[a-z0-9_]+)+[0-9a-z_]$";

    private HashSet<String> withPousseCafeBasePackages(Collection<String> basePackages) {
        var completedBasePackages = new HashSet<>(basePackages);
        completedBasePackages.add("poussecafe.messaging");
        completedBasePackages.add("poussecafe.runtime");
        completedBasePackages.add("poussecafe.domain");
        return completedBasePackages;
    }

    private Reflections reflections;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<AggregateDefinition> discoverAggregates(Class<? extends Module> moduleClass) {
        List<Class<?>> aggregateClasses = reflections.getTypesAnnotatedWith(Aggregate.class).stream()
                .filter(aggregateClass -> isInModule(aggregateClass, moduleClass))
                .collect(toList());

        List<AggregateDefinition> definitions = new ArrayList<>();
        for(Class<?> aggregateClass : aggregateClasses) {
            Aggregate annotation = aggregateClass.getAnnotation(Aggregate.class);

            var definitionBuilder = new AggregateDefinition.Builder()
                    .withModuleClass(moduleClass);

            if(AggregateRoot.class.isAssignableFrom(aggregateClass)) {
                definitionBuilder.withAggregateRoot((Class<? extends AggregateRoot>) aggregateClass);

                if(annotation.factory() == InnerClassFactory.class) {
                    throw new PousseCafeException("@Aggregate on aggregate root class requires factory attribute to be set");
                } else {
                    definitionBuilder.withFactoryClass(annotation.factory());
                }

                if(annotation.repository() == InnerClassRepository.class) {
                    throw new PousseCafeException("@Aggregate on aggregate root class requires repository attribute to be set");
                } else {
                    definitionBuilder.withRepositoryClass(annotation.repository());
                }
            } else {
                var containerClass = new AggregateContainerClass(aggregateClass);
                definitionBuilder.withAggregateRoot(containerClass.aggregateRootClass());
                definitionBuilder.withFactoryClass(containerClass.factoryClass());
                definitionBuilder.withRepositoryClass(containerClass.repositoryClass());
            }

            definitions.add(definitionBuilder.build());
        }

        return definitions;
    }

    private boolean isInModule(Class<?> componentClass, Class<? extends Module> moduleClass) {
        return componentClass.getPackageName().startsWith(moduleClass.getPackageName());
    }

    private Logger logger = LoggerFactory.getLogger(ClassPathExplorer.class);

    public List<Class<? extends Service>> discoverServices(Class<? extends Module> module) {
        return getSubTypesOf(Service.class)
                .filter(serviceClass -> !serviceClass.isAnnotationPresent(ServiceImplementation.class))
                .filter(serviceClass -> isInModule(serviceClass, module))
                .collect(toList());
    }

    public List<Class<? extends DomainProcess>> discoverDomainProcesses(Class<? extends Module> module) {
        return getSubTypesOf(DomainProcess.class)
                .filter(processClass -> isInModule(processClass, module))
                .collect(toList());
    }

    public List<Class<? extends Message>> discoverMessages(Class<? extends Module> module) {
        List<Class<? extends Message>> messages = getSubTypesOf(Message.class)
                .filter(this::isMessageDefinition)
                .filter(messageClass -> isInModule(messageClass, module))
                .filter(this::hasNotAbstractMessageAnnotation)
                .collect(toList());
        if(logger.isDebugEnabled()) {
            messages.forEach(messageClass -> logger.debug("Adding message definition {}", messageClass));
        }
        return messages;
    }

    private boolean isMessageDefinition(Class<? extends Message> messageClass) {
        MessageImplementation annotation = messageClass.getAnnotation(MessageImplementation.class);
        if(annotation == null) {
            return messageClass.isInterface()
                    && messageClass != DomainEvent.class
                    && messageClass != Command.class;
        } else {
            return annotation.message() == messageClass;
        }
    }

    private <C> Stream<Class<? extends C>> getSubTypesOf(Class<C> type) {
        return reflections.getSubTypesOf(type).stream();
    }

    private boolean hasNotAbstractMessageAnnotation(Class<? extends Message> messageClass) {
        return asList(messageClass.getAnnotations()).stream()
                .map(Annotation::annotationType)
                .noneMatch(annotationType -> annotationType == AbstractMessage.class);
    }

    @SuppressWarnings("unchecked")
    public Set<Class<Message>> getMessageImplementations(Messaging messaging) {
        Set<Class<?>> implementationClasses = reflections.getTypesAnnotatedWith(MessageImplementation.class);

        Set<Class<Message>> messageImplementationClasses = new HashSet<>();
        for(Class<?> messageImplementationClass : implementationClasses) {
            MessageImplementation annotation = messageImplementationClass.getAnnotation(MessageImplementation.class);
            if(annotation.messagingNames().length == 0 ||
                    messaging.nameIn(annotation.messagingNames())) {
                logger.debug("Adding message implementation {}", messageImplementationClass);
                messageImplementationClasses.add((Class<Message>) messageImplementationClass);
            }
        }
        return messageImplementationClasses;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Set<Class<EntityDataAccess>> getDataAccessImplementations(Storage storage) {
        Set<Class<?>> dataAccessImplementations = reflections.getTypesAnnotatedWith(DataAccessImplementation.class);
        Set<Class<?>> aggregateRoots = new HashSet<>();

        Set<Class<EntityDataAccess>> entityDataAccessClasses = new HashSet<>();
        for(Class<?> entityDataAccessClass : dataAccessImplementations) {
            DataAccessImplementation annotation = entityDataAccessClass.getAnnotation(DataAccessImplementation.class);
            if(storage.name().equals(entityDataAccessClass.getAnnotation(DataAccessImplementation.class).storageName())) {
                if(!aggregateRoots.add(annotation.aggregateRoot())) {
                    throw new PousseCafeException("Aggregate root " + annotation.aggregateRoot() + " has already an implementation");
                }

                logger.debug("Adding data access implementation {}", entityDataAccessClass);
                entityDataAccessClasses.add((Class<EntityDataAccess>) entityDataAccessClass);
            }
        }
        return entityDataAccessClasses;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Set<Class<EntityAttributes>> getDataImplementations(Storage storage) {
        Set<Class<?>> dataImplementations = reflections.getTypesAnnotatedWith(DataImplementation.class);

        Set<Class<EntityAttributes>> entityDataClasses = new HashSet<>();
        for(Class<?> entityDataClass : dataImplementations) {
            DataImplementation annotation = entityDataClass.getAnnotation(DataImplementation.class);
            if(annotation.storageNames().length == 0 ||
                    storage.nameIn(annotation.storageNames())) {
                logger.debug("Adding data implementation {}", entityDataClass);
                entityDataClasses.add((Class<EntityAttributes>) entityDataClass);
            }
        }
        return entityDataClasses;
    }

    @SuppressWarnings("unchecked")
    public Set<poussecafe.environment.ServiceImplementation> discoverServiceImplementations(Class<? extends Module> module) {
        List<Class<?>> serviceImplementations = reflections.getTypesAnnotatedWith(ServiceImplementation.class).stream()
                .filter(serviceClass -> isInModule(serviceClass, module))
                .collect(toList());
        Set<poussecafe.environment.ServiceImplementation> implementations = new HashSet<>();
        for(Class<?> serviceImplementationClass : serviceImplementations) {
            ServiceImplementation annotation = serviceImplementationClass.getAnnotation(ServiceImplementation.class);
            logger.debug("Adding service implementation {}", serviceImplementationClass);
            implementations.add(new poussecafe.environment.ServiceImplementation.Builder()
                    .serviceClass(annotation.service())
                    .serviceImplementationClass((Class<? extends Service>) serviceImplementationClass)
                    .build());
        }
        return implementations;
    }

    public Set<MessageListenerDefinition> discoverListeners(Class<? extends Module> module) {
        Set<Class<?>> listenersContainers = new HashSet<>();
        getSubTypesOf(DomainProcess.class)
                .filter(aggregateClass -> isInModule(aggregateClass, module))
                .forEach(listenersContainers::add);
        getSubTypesOf(AggregateRoot.class)
                .filter(aggregateClass -> isInModule(aggregateClass, module))
                .forEach(listenersContainers::add);
        getSubTypesOf(AggregateFactory.class)
                .filter(aggregateClass -> isInModule(aggregateClass, module))
                .forEach(listenersContainers::add);
        getSubTypesOf(AggregateRepository.class)
                .filter(aggregateClass -> isInModule(aggregateClass, module))
                .forEach(listenersContainers::add);

        Set<MessageListenerDefinition> definitions = new HashSet<>();
        for(Class<?> containerClass : listenersContainers) {
            if(!Modifier.isAbstract(containerClass.getModifiers())) {
                definitions.addAll(new MessageListenerDefinitionDiscoverer(containerClass).discoverListenersOfClass());
            }
        }
        return definitions;
    }
}
