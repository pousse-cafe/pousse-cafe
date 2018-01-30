package poussecafe.storable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import poussecafe.exception.PousseCafeException;
import poussecafe.storage.Storage;

import static java.util.Collections.unmodifiableSet;
import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class Environment {

    public void defineStorable(StorableDefinition definition) {
        checkThat(value(definition).notNull());
        Class<?> storableClass = definition.getStorableClass();
        if(definitions.containsKey(storableClass)) {
            throw new PousseCafeException("Storable " + storableClass.getName() + " is already defined");
        }
        definitions.put(storableClass, definition);

        if(definition.hasFactory()) {
            storableClassByRelated.put(definition.getFactoryClass(), storableClass);
        }

        if(definition.hasRepository()) {
            storableClassByRelated.put(definition.getRepositoryClass(), storableClass);
        }
    }

    private Map<Class<?>, StorableDefinition> definitions = new HashMap<>();

    private Map<Class<?>, Class<?>> storableClassByRelated = new HashMap<>();

    public void implementStorable(StorableImplementation implementation) {
        checkThat(value(implementation).notNull());
        Class<?> storableClass = implementation.getStorableClass();
        if(!definitions.containsKey(storableClass)) {
            throw new PousseCafeException("Storable " + storableClass.getName() + " is not defined");
        }
        implementations.put(storableClass, implementation);

        if(implementation.hasStorage()) {
            storages.add(implementation.getStorage());
        }
    }

    private Map<Class<?>, StorableImplementation> implementations = new HashMap<>();

    private Set<Storage> storages = new HashSet<>();

    public boolean isAbstract() {
        return !getAbstractStorables().isEmpty();
    }

    public Set<Class<?>> getAbstractStorables() {
        Set<Class<?>> abstractStorables = new HashSet<>();
        for(Class<?> storableClass : definitions.keySet()) {
            if(!implementations.containsKey(storableClass)) {
                abstractStorables.add(storableClass);
            }
        }
        return abstractStorables;
    }

    public Storage getStorage(Class<?> storableClass) {
        StorableImplementation implementation = getStorableImplementation(storableClass);
        return implementation.getStorage();
    }

    public StorableImplementation getStorableImplementation(Class<?> storableClass) {
        StorableImplementation implementation = implementations.get(storableClass);
        if(implementation == null) {
            throw new PousseCafeException("Storable " + storableClass.getName() + " is not implemented");
        }
        return implementation;
    }

    public Supplier<Object> getStorableDataFactory(Class<?> storableClass) {
        StorableImplementation implementation = getStorableImplementation(storableClass);
        return implementation.getDataFactory();
    }

    public Class<?> getStorableClass(Class<?> relatedClass) {
        Class<?> storableClass = storableClassByRelated.get(relatedClass);
        if(storableClass == null) {
            throw new PousseCafeException("No storable for related class " + relatedClass.getName());
        }
        return storableClass;
    }

    public Supplier<Object> getStorableDataAccessFactory(Class<?> storableClass) {
        StorableImplementation implementation = getStorableImplementation(storableClass);
        if(!implementation.hasDataAccess()) {
            throw new PousseCafeException("Storable " + storableClass + " has no data access");
        }
        return implementation.getDataAccessFactory();
    }

    public StorableDefinition getStorableDefinition(Class<?> storableClass) {
        StorableDefinition definition = definitions.get(storableClass);
        if(definition == null) {
            throw new PousseCafeException("Storable " + storableClass + " is not defined");
        }
        return definition;
    }

    public Set<Class<?>> getDefinedStorables() {
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

    public void defineProcess(Class<?> processClass) {
        checkThat(value(processClass).notNull());
        processes.add(processClass);
    }

    private Set<Class<?>> processes = new HashSet<>();

    public Set<Class<?>> getDefinedProcesses() {
        return unmodifiableSet(processes);
    }

    public Set<Storage> getStorages() {
        return unmodifiableSet(storages);
    }
}
