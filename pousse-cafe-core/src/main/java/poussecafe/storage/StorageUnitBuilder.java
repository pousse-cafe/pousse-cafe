package poussecafe.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.domain.EntityData;
import poussecafe.domain.EntityDataAccess;
import poussecafe.domain.EntityImplementation;
import poussecafe.exception.PousseCafeException;

import static java.util.stream.Collectors.toList;
import static poussecafe.check.Checks.checkThatValue;

public class StorageUnitBuilder {

    StorageUnitBuilder(Storage storage) {
        checkThatValue(storage).notNull();
        this.storage = storage;
    }

    private Storage storage;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public StorageUnitBuilder withPackage(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> dataAccessImplementations = reflections.getTypesAnnotatedWith(DataAccessImplementation.class);
        Set<Class<?>> aggregateRoots = new HashSet<>();
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

        Set<Class<?>> dataImplementations = reflections.getTypesAnnotatedWith(DataImplementation.class);
        for(Class<?> entityDataClass : dataImplementations) {
            DataImplementation annotation = entityDataClass.getAnnotation(DataImplementation.class);
            if(annotation.storageNames().length == 0 ||
                    storage.nameIn(annotation.storageNames())) {
                if(aggregateRoots.contains(annotation.entity())) {
                    throw new PousseCafeException("Aggregate root implementation can only be declared with @" + DataAccessImplementation.class.getSimpleName());
                }

                logger.debug("Adding data implementation {}", entityDataClass);
                entityDataClasses.add((Class<EntityData>) entityDataClass);
            }
        }
        return this;
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    @SuppressWarnings("rawtypes")
    private Set<Class<EntityDataAccess>> entityDataAccessClasses = new HashSet<>();

    @SuppressWarnings("rawtypes")
    private Set<Class<EntityData>> entityDataClasses = new HashSet<>();

    public StorageUnit build() {
        StorageUnit unit = new StorageUnit();
        unit.storage = storage;
        unit.implementations = new ArrayList<>();

        List<EntityImplementation> aggregateRootImplementations = entityDataAccessClasses.stream()
            .map(this::buildAggregateRootImplementation)
            .collect(toList());
        unit.implementations.addAll(aggregateRootImplementations);

        List<EntityImplementation> nonRootEntityImplementations = entityDataClasses.stream()
                .map(this::buildNonRootEntityImplementation)
                .collect(toList());
        unit.implementations.addAll(nonRootEntityImplementations);

        return unit;
    }

    @SuppressWarnings("rawtypes")
    private EntityImplementation buildAggregateRootImplementation(Class<EntityDataAccess> entityDataAccessClass) {
        DataAccessImplementation annotation = entityDataAccessClass.getAnnotation(DataAccessImplementation.class);
        return new EntityImplementation.Builder()
                .withEntityClass(annotation.aggregateRoot())
                .withDataAccessFactory(() -> newInstance(entityDataAccessClass))
                .withDataFactory(() -> newInstance(annotation.dataImplementation()))
                .withStorage(storage)
                .build();
    }

    private <T> T newInstance(Class<T> aClass) {
        try {
            return aClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new PousseCafeException("Class " + aClass.getName() + " is missing a public constructor with no arguments");
        }
    }

    @SuppressWarnings("rawtypes")
    private EntityImplementation buildNonRootEntityImplementation(Class<EntityData> entityDataClass) {
        DataImplementation annotation = entityDataClass.getAnnotation(DataImplementation.class);
        return new EntityImplementation.Builder()
                .withEntityClass(annotation.entity())
                .withDataFactory(() -> newInstance(entityDataClass))
                .withStorage(storage)
                .build();
    }
}