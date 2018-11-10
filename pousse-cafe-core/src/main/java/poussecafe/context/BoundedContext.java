package poussecafe.context;

import java.util.HashSet;
import java.util.Set;
import poussecafe.domain.EntityDefinition;
import poussecafe.domain.EntityImplementation;
import poussecafe.domain.Service;
import poussecafe.process.DomainProcess;

import static java.util.Collections.unmodifiableSet;

public abstract class BoundedContext {

    protected BoundedContext() {
        this(true);
    }

    protected BoundedContext(boolean loadAll) {
        if(loadAll) {
            loadAll();
        }
    }

    protected void loadAll() {
        loadDefinitions(definitions);
        loadImplementations(implementations);
        loadProcesses(processes);
        loadServices(services);
    }

    private Set<EntityDefinition> definitions = new HashSet<>();

    protected abstract void loadDefinitions(Set<EntityDefinition> definitions);

    private Set<EntityImplementation> implementations = new HashSet<>();

    protected abstract void loadImplementations(Set<EntityImplementation> implementations);

    private Set<Class<? extends DomainProcess>> processes = new HashSet<>();

    protected abstract void loadProcesses(Set<Class<? extends DomainProcess>> processes);

    private Set<Class<? extends Service>> services = new HashSet<>();

    protected abstract void loadServices(Set<Class<? extends Service>> services);

    public Set<EntityDefinition> getDefinitions() {
        return unmodifiableSet(definitions);
    }

    public Set<EntityImplementation> getImplementations() {
        return unmodifiableSet(implementations);
    }

    public Set<Class<? extends DomainProcess>> getProcesses() {
        return unmodifiableSet(processes);
    }

    public Set<Class<? extends Service>> getServices() {
        return unmodifiableSet(services);
    }
}
