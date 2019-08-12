package poussecafe.discovery;

import com.google.common.base.Predicate;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;
import poussecafe.domain.EntityDataAccess;
import poussecafe.domain.Factory;
import poussecafe.domain.Repository;
import poussecafe.domain.Service;
import poussecafe.environment.AggregateDefinition;
import poussecafe.environment.AggregateMessageListenerRunner;
import poussecafe.environment.MessageListenerDefinition;
import poussecafe.exception.PousseCafeException;
import poussecafe.messaging.Message;
import poussecafe.messaging.Messaging;
import poussecafe.process.DomainProcess;
import poussecafe.storage.Storage;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

class ClassPathExplorer {

    ClassPathExplorer(Collection<String> packagePrefix) {
        packagePrefixes = new HashSet<>(packagePrefix);

        ConfigurationBuilder reflectionsConfigurationBuilder = ConfigurationBuilder.build(packagePrefixes.toArray());
        reflectionsConfigurationBuilder.filterInputsBy(onlyByteCodeFiles);
        reflections = new Reflections(reflectionsConfigurationBuilder);
    }

    private Set<String> packagePrefixes = new HashSet<>();

    private Predicate<String> onlyByteCodeFiles = input -> input.endsWith(".class") || input.endsWith(".jar");

    private Reflections reflections;

    public List<AggregateDefinition> discoverDefinitions() {
        Set<Class<?>> aggregateRootClasses = getTypesAnnotatedWith(Aggregate.class);

        List<AggregateDefinition> definitions = new ArrayList<>();
        for(Class<?> aggregateRootClass : aggregateRootClasses) {
            if(!AggregateRoot.class.isAssignableFrom(aggregateRootClass)) {
                throw new PousseCafeException("Only aggregate roots can be annotated with @" + Aggregate.class.getSimpleName());
            }

            Aggregate annotation = aggregateRootClass.getAnnotation(Aggregate.class);
            definitions.add(new AggregateDefinition.Builder()
                    .withAggregateRoot(aggregateRootClass)
                    .withFactoryClass(annotation.factory())
                    .withRepositoryClass(annotation.repository())
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
        return getSubTypesOf(Message.class)
                .filter(this::isMessageDefinition)
                .filter(this::hasNotAbstractMessageAnnotation)
                .collect(toList());
    }

    private boolean isMessageDefinition(Class<? extends Message> messageClass) {
        MessageImplementation annotation = messageClass.getAnnotation(MessageImplementation.class);
        if(annotation == null) {
            return messageClass.isInterface();
        } else {
            return annotation.message() == messageClass;
        }
    }

    private <C> Stream<Class<? extends C>> getSubTypesOf(Class<C> type) {
        return reflections.getSubTypesOf(type)
                .stream()
                .filter(this::inGivenPackages);
    }

    private boolean inGivenPackages(Class<?> aClass) {
        for(String packagePrefix : packagePrefixes) {
            if(aClass.getPackage().getName().startsWith(packagePrefix)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasNotAbstractMessageAnnotation(Class<? extends Message> messageClass) {
        return asList(messageClass.getAnnotations()).stream()
                .map(Annotation::annotationType)
                .noneMatch(annotationType -> annotationType == AbstractMessage.class);
    }

    @SuppressWarnings("unchecked")
    public Set<Class<Message>> getMessageImplementations(Messaging messaging) {
        Set<Class<?>> implementationClasses = getTypesAnnotatedWith(MessageImplementation.class);

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

    public Set<Class<?>> getTypesAnnotatedWith(final Class<? extends Annotation> annotation) {
        return reflections.getTypesAnnotatedWith(annotation)
                .stream()
                .filter(this::inGivenPackages)
                .collect(toSet());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Set<Class<EntityDataAccess>> getDataAccessImplementations(Storage storage) {
        Set<Class<?>> dataAccessImplementations = getTypesAnnotatedWith(DataAccessImplementation.class);
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

    @SuppressWarnings("unchecked")
    public Set<Class<EntityAttributes<?>>> getDataImplementations(Storage storage) {
        Set<Class<?>> dataImplementations = getTypesAnnotatedWith(DataImplementation.class);

        Set<Class<EntityAttributes<?>>> entityDataClasses = new HashSet<>();
        for(Class<?> entityDataClass : dataImplementations) {
            DataImplementation annotation = entityDataClass.getAnnotation(DataImplementation.class);
            if(annotation.storageNames().length == 0 ||
                    storage.nameIn(annotation.storageNames())) {
                logger.debug("Adding data implementation {}", entityDataClass);
                entityDataClasses.add((Class<EntityAttributes<?>>) entityDataClass);
            }
        }
        return entityDataClasses;
    }

    @SuppressWarnings("unchecked")
    public Set<poussecafe.environment.ServiceImplementation> discoverServiceImplementations() {
        Set<Class<?>> serviceImplementations = getTypesAnnotatedWith(ServiceImplementation.class);
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
                definitions.addAll(discoverListenersOfClass(containerClass));
            }
        }
        return definitions;
    }

    private Collection<MessageListenerDefinition> discoverListenersOfClass(Class<?> containerClass) {
        List<MessageListenerDefinition> definitions = new ArrayList<>();
        for(Method method : containerClass.getMethods()) {
            MessageListener annotation = method.getAnnotation(MessageListener.class);
            if(annotation != null) {
                logger.debug("Defining listener for method {} of {}", method, containerClass);
                definitions.add(new MessageListenerDefinition.Builder()
                        .containerClass(containerClass)
                        .method(method)
                        .customId(customId(annotation.id()))
                        .runner(runner(annotation.runner()))
                        .build());
            }
        }
        return definitions;
    }

    private Optional<String> customId(String id) {
        if(id == null || id.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(id);
        }
    }

    private Optional<Class<? extends AggregateMessageListenerRunner<?, ?, ?>>> runner(
            Class<? extends AggregateMessageListenerRunner<?, ?, ?>> runner) {
        if(runner == VoidAggregateMessageListenerRunner.class) {
            return Optional.empty();
        } else {
            return Optional.of(runner);
        }
    }
}
