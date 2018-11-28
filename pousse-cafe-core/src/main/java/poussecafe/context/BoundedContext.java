package poussecafe.context;

import java.util.HashSet;
import java.util.Set;
import poussecafe.domain.EntityDefinition;
import poussecafe.domain.EntityImplementation;
import poussecafe.domain.Service;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageImplementationConfiguration;
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
        loadEntityImplementations(entityImplementations);
        loadMessageImplementations(messageImplementations);
        loadProcesses(processes);
        loadServices(services);
        loadMessages(messages);
    }

    private Set<EntityDefinition> definitions = new HashSet<>();

    protected abstract void loadDefinitions(Set<EntityDefinition> definitions);

    public Set<EntityDefinition> getDefinitions() {
        return unmodifiableSet(definitions);
    }

    private Set<EntityImplementation> entityImplementations = new HashSet<>();

    protected abstract void loadEntityImplementations(Set<EntityImplementation> implementations);

    public Set<EntityImplementation> getEntityImplementations() {
        return unmodifiableSet(entityImplementations);
    }

    private Set<MessageImplementationConfiguration> messageImplementations = new HashSet<>();

    protected abstract void loadMessageImplementations(Set<MessageImplementationConfiguration> implementations);

    public Set<MessageImplementationConfiguration> getMessageImplementations() {
        return unmodifiableSet(messageImplementations);
    }

    private Set<Class<? extends DomainProcess>> processes = new HashSet<>();

    protected abstract void loadProcesses(Set<Class<? extends DomainProcess>> processes);

    public Set<Class<? extends DomainProcess>> getProcesses() {
        return unmodifiableSet(processes);
    }

    private Set<Class<? extends Service>> services = new HashSet<>();

    protected abstract void loadServices(Set<Class<? extends Service>> services);

    public Set<Class<? extends Service>> getServices() {
        return unmodifiableSet(services);
    }

    private Set<Class<? extends Message>> messages = new HashSet<>();

    protected abstract void loadMessages(Set<Class<? extends Message>> messages);

    public Set<Class<? extends Message>> getMessages() {
        return unmodifiableSet(messages);
    }
}
