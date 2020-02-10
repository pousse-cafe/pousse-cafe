package poussecafe.discovery;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.DomainEvent;
import poussecafe.domain.EntityAttributes;
import poussecafe.domain.EntityDataAccess;
import poussecafe.domain.Factory;
import poussecafe.domain.Module;
import poussecafe.domain.Repository;
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

    ClassPathExplorer(Collection<String> packagePrefixes) {
        this.packagePrefixes = new HashSet<>(packagePrefixes);
        this.packagePrefixes.add("poussecafe.messaging");
        this.packagePrefixes.add("poussecafe.runtime");
        this.packagePrefixes.add("poussecafe.domain");

        ConfigurationBuilder reflectionsConfigurationBuilder = new ConfigurationBuilder();
        FilterBuilder filter = new FilterBuilder();
        ClassLoader[] classLoaders = null;
        for(String packagePrefix : this.packagePrefixes) {
            filter.includePackage(packagePrefix);
            reflectionsConfigurationBuilder.addUrls(ClasspathHelper.forPackage(packagePrefix, classLoaders));
        }
        filter.exclude(".*\\.java");

        reflectionsConfigurationBuilder.filterInputsBy(filter);
        reflectionsConfigurationBuilder.setExpandSuperTypes(false);
        reflections = new Reflections(reflectionsConfigurationBuilder);
    }

    private Set<String> packagePrefixes = new HashSet<>();

    private Reflections reflections;

    public List<AggregateDefinition> discoverAggregates() {
        Set<Class<?>> aggregateRootClasses = reflections.getTypesAnnotatedWith(Aggregate.class);

        List<AggregateDefinition> definitions = new ArrayList<>();
        for(Class<?> aggregateRootClass : aggregateRootClasses) {
            if(!AggregateRoot.class.isAssignableFrom(aggregateRootClass)) {
                throw new PousseCafeException("Only aggregate roots can be annotated with @" + Aggregate.class.getSimpleName());
            }

            Aggregate annotation = aggregateRootClass.getAnnotation(Aggregate.class);
            Optional<Class<? extends Module>> moduleClass;
            if(annotation.module() == DefaultModule.class) {
                moduleClass = Optional.empty();
            } else {
                String aggregateRootPackage = aggregateRootClass.getPackageName();
                String moduleClassPackage = annotation.module().getPackageName();
                if(!aggregateRootPackage.startsWith(moduleClassPackage)) {
                    throw new PousseCafeException("Aggregate root " + aggregateRootClass.getName() + " is not in a sub-package of its module class package " + moduleClassPackage);
                }
                moduleClass = Optional.of(annotation.module());
            }

            definitions.add(new AggregateDefinition.Builder()
                    .withAggregateRoot(aggregateRootClass)
                    .withFactoryClass(annotation.factory())
                    .withRepositoryClass(annotation.repository())
                    .withModuleClass(moduleClass)
                    .build());
        }
        return definitions;
    }

    private Logger logger = LoggerFactory.getLogger(ClassPathExplorer.class);

    public List<Class<? extends Service>> discoverServices() {
        return getSubTypesOf(Service.class)
                .filter(serviceClass -> !serviceClass.isAnnotationPresent(ServiceImplementation.class))
                .collect(toList());
    }

    public List<Class<? extends DomainProcess>> discoverDomainProcesses() {
        return getSubTypesOf(DomainProcess.class).collect(toList());
    }

    public List<Class<? extends Message>> discoverMessages() {
        List<Class<? extends Message>> messages = getSubTypesOf(Message.class)
                .filter(this::isMessageDefinition)
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
    public Set<poussecafe.environment.ServiceImplementation> discoverServiceImplementations() {
        Set<Class<?>> serviceImplementations = reflections.getTypesAnnotatedWith(ServiceImplementation.class);
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

    public Set<MessageListenerDefinition> discoverListeners() {
        Set<Class<?>> listenersContainers = new HashSet<>();
        getSubTypesOf(DomainProcess.class).forEach(listenersContainers::add);
        getSubTypesOf(AggregateRoot.class).forEach(listenersContainers::add);
        getSubTypesOf(Factory.class).forEach(listenersContainers::add);
        getSubTypesOf(Repository.class).forEach(listenersContainers::add);

        Set<MessageListenerDefinition> definitions = new HashSet<>();
        for(Class<?> containerClass : listenersContainers) {
            if(!Modifier.isAbstract(containerClass.getModifiers())) {
                definitions.addAll(new MessageListenerDefinitionDiscoverer(containerClass).discoverListenersOfClass());
            }
        }
        return definitions;
    }
}
