package poussecafe.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityDefinition;
import poussecafe.domain.Factory;
import poussecafe.domain.Repository;
import poussecafe.domain.Service;
import poussecafe.messaging.MessageImplementation;
import poussecafe.process.DomainProcess;

import static java.util.stream.Collectors.toList;

public class BoundedContextDiscovery {

    private BoundedContextDiscovery() {

    }

    @SuppressWarnings("rawtypes")
    public static List<EntityDefinition> discoverDefinitions(String packageName) {
        logger.info("Discovering definitions in package {}", packageName);
        Reflections reflections = new Reflections(packageName);

        Map<String, Class<? extends AggregateRoot>> aggregateRootClassesByName = classesByName(reflections, AggregateRoot.class);
        Map<String, Class<? extends Factory>> factoryClassesByName = classesByName(reflections, Factory.class);
        Map<String, Class<? extends Repository>> repositoryClassesByName = classesByName(reflections, Repository.class);

        List<EntityDefinition> discoveredDefinitions = new ArrayList<>();
        for(Entry<String, Class<? extends AggregateRoot>> entry : aggregateRootClassesByName.entrySet()) {
            String aggregateRootName = entry.getKey();
            String expectedFactoryName = entry.getKey() + "Factory";
            String expectedRepositoryName = entry.getKey() + "Repository";

            Class<? extends Factory> factoryClass = factoryClassesByName.get(expectedFactoryName);
            if(factoryClass == null) {
                logger.debug("Skipping aggregate {}: no factory class found with name {}", aggregateRootName, expectedFactoryName);
                continue;
            }

            Class<? extends Repository> repositoryClass = repositoryClassesByName.get(expectedRepositoryName);
            if(repositoryClass == null) {
                logger.debug("Skipping aggregate {}: no repository class found with name {}", aggregateRootName, expectedRepositoryName);
                continue;
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

    private static Logger logger = LoggerFactory.getLogger(BoundedContextDiscovery.class);

    public static <T> Map<String, Class<? extends T>> classesByName(Reflections reflections, Class<T> superType) {
        Set<Class<? extends T>> aggregateRootClasses = reflections.getSubTypesOf(superType);
        Map<String, Class<? extends T>> classesByName = new HashMap<>();
        for(Class<? extends T> aClass : aggregateRootClasses) {
            String className = aClass.getSimpleName();
            classesByName.put(className, aClass);
        }
        return classesByName;
    }

    public static List<Class<? extends Service>> discoverServices(String packageName) {
        logger.info("Discovering services in package {}", packageName);
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends Service>> serviceClasses = reflections.getSubTypesOf(Service.class);
        return serviceClasses.stream().collect(toList());
    }

    public static List<Class<? extends DomainProcess>> discoverDomainProcesses(String packageName) {
        logger.info("Discovering domain processes in package {}", packageName);
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends DomainProcess>> domainProcessClasses = reflections.getSubTypesOf(DomainProcess.class);
        return domainProcessClasses.stream().collect(toList());
    }

    public static List<poussecafe.domain.MessageImplementation> discoverMessageImplementations(String packageName) {
        logger.info("Discovering message implementations in package {}", packageName);
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> messageImplementationClasses = reflections.getTypesAnnotatedWith(MessageImplementation.class);
        return messageImplementationClasses.stream()
                .map(BoundedContextDiscovery::messageImplementation)
                .collect(toList());
    }

    private static poussecafe.domain.MessageImplementation messageImplementation(Class<?> messageImplementationClass) {
        MessageImplementation annotation = messageImplementationClass.getAnnotation(MessageImplementation.class);
        return new poussecafe.domain.MessageImplementation.Builder()
                .withMessageClass(annotation.message())
                .withMessageImplementationClass(messageImplementationClass)
                .build();
    }
}
