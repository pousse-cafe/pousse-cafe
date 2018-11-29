package poussecafe.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityData;
import poussecafe.domain.EntityDataAccess;
import poussecafe.domain.EntityDefinition;
import poussecafe.domain.Factory;
import poussecafe.domain.Repository;
import poussecafe.domain.Service;
import poussecafe.exception.PousseCafeException;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageImplementation;
import poussecafe.messaging.Messaging;
import poussecafe.process.DomainProcess;
import poussecafe.storage.DataAccessImplementation;
import poussecafe.storage.DataImplementation;
import poussecafe.storage.Storage;

import static java.util.stream.Collectors.toList;

public class ClassPathExplorer {

    ClassPathExplorer(Collection<String> packagePrefix) {
        packagePrefixes = new HashSet<>(packagePrefix);
        reflections = new Reflections(packagePrefixes.toArray());
    }

    private Set<String> packagePrefixes = new HashSet<>();

    private Reflections reflections;

    @SuppressWarnings("rawtypes")
    public List<EntityDefinition> discoverDefinitions() {
        Map<String, Class<? extends AggregateRoot>> aggregateRootClassesByName = classesByName(AggregateRoot.class);
        Map<String, Class<? extends Factory>> factoryClassesByName = classesByName(Factory.class);
        Map<String, Class<? extends Repository>> repositoryClassesByName = classesByName(Repository.class);

        List<EntityDefinition> discoveredDefinitions = new ArrayList<>();
        for(Entry<String, Class<? extends AggregateRoot>> entry : aggregateRootClassesByName.entrySet()) {
            Class<? extends AggregateRoot> aggregateRootClass = entry.getValue();
            String aggregateRootName = aggregateRootClass.getSimpleName();
            String aggregateRootPackage = aggregateRootClass.getPackage().getName();

            String expectedFactoryName = aggregateRootPackage + "." + aggregateRootName + "Factory";
            Class<? extends Factory> factoryClass = factoryClassesByName.get(expectedFactoryName);
            if(factoryClass == null) {
                throw new PousseCafeException("Missing factory class " + expectedFactoryName);
            }

            String expectedRepositoryName = aggregateRootPackage + "." + aggregateRootName + "Repository";
            Class<? extends Repository> repositoryClass = repositoryClassesByName.get(expectedRepositoryName);
            if(repositoryClass == null) {
                throw new PousseCafeException("Missing repository class " + expectedRepositoryName);
            }

            EntityDefinition definition = new EntityDefinition.Builder()
                    .withEntityClass(entry.getValue())
                    .withFactoryClass(factoryClass)
                    .withRepositoryClass(repositoryClass)
                    .build();

            logger.debug("Adding definition for aggregate {}", aggregateRootName);
            discoveredDefinitions.add(definition);
        }
        return discoveredDefinitions;
    }

    private Logger logger = LoggerFactory.getLogger(ClassPathExplorer.class);

    private <T> Map<String, Class<? extends T>> classesByName(Class<T> superType) {
        Map<String, Class<? extends T>> classesByName = new HashMap<>();
        getSubTypesOf(superType).forEach(aClass -> {
            String className = aClass.getName();
            classesByName.put(className, aClass);
        });
        return classesByName;
    }

    public List<Class<? extends Service>> discoverServices() {
        return getSubTypesOf(Service.class).collect(toList());
    }

    public List<Class<? extends DomainProcess>> discoverDomainProcesses() {
        return getSubTypesOf(DomainProcess.class).collect(toList());
    }

    public List<Class<? extends Message>> discoverMessages() {
        return getSubTypesOf(Message.class)
                .filter(Class::isInterface)
                .collect(toList());
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

    @SuppressWarnings("unchecked")
    public Set<Class<EntityData<?>>> getDataImplementations(Storage storage) {
        Set<Class<?>> dataImplementations = reflections.getTypesAnnotatedWith(DataImplementation.class);

        Set<Class<EntityData<?>>> entityDataClasses = new HashSet<>();
        for(Class<?> entityDataClass : dataImplementations) {
            DataImplementation annotation = entityDataClass.getAnnotation(DataImplementation.class);
            if(annotation.storageNames().length == 0 ||
                    storage.nameIn(annotation.storageNames())) {
                logger.debug("Adding data implementation {}", entityDataClass);
                entityDataClasses.add((Class<EntityData<?>>) entityDataClass);
            }
        }
        return entityDataClasses;
    }
}
