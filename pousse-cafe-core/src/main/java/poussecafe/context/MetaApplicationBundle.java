package poussecafe.context;

import java.util.HashSet;
import java.util.Set;
import poussecafe.service.DomainProcess;
import poussecafe.storable.StorableDefinition;
import poussecafe.storable.StorableImplementation;

import static java.util.Collections.unmodifiableSet;

public abstract class MetaApplicationBundle {

    protected MetaApplicationBundle() {
        loadDefinitions(definitions);
        loadImplementations(implementations);
        loadProcesses(processes);
        loadServices(services);
    }

    private Set<StorableDefinition> definitions = new HashSet<>();

    protected abstract void loadDefinitions(Set<StorableDefinition> definitions);

    private Set<StorableImplementation> implementations = new HashSet<>();

    protected abstract void loadImplementations(Set<StorableImplementation> implementations);

    private Set<Class<? extends DomainProcess>> processes = new HashSet<>();

    protected abstract void loadProcesses(Set<Class<? extends DomainProcess>> processes);

    private Set<Class<?>> services = new HashSet<>();

    protected abstract void loadServices(Set<Class<?>> services);

    public Set<StorableDefinition> getDefinitions() {
        return unmodifiableSet(definitions);
    }

    public Set<StorableImplementation> getImplementations() {
        return unmodifiableSet(implementations);
    }

    public Set<Class<? extends DomainProcess>> getProcesses() {
        return unmodifiableSet(processes);
    }

    public Set<Class<?>> getServices() {
        return unmodifiableSet(services);
    }
}
