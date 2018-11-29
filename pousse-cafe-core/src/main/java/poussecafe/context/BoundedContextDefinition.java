package poussecafe.context;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import poussecafe.domain.EntityDefinition;
import poussecafe.domain.Service;
import poussecafe.messaging.Message;
import poussecafe.process.DomainProcess;

import static java.util.Collections.unmodifiableSet;

public class BoundedContextDefinition {

    public static class Builder {

        private BoundedContextDefinition definition = new BoundedContextDefinition();

        public Builder definitions(Collection<EntityDefinition> definitions) {
            definition.definitions.addAll(definitions);
            return this;
        }

        public Builder processes(Collection<Class<? extends DomainProcess>> processes) {
            definition.processes.addAll(processes);
            return this;
        }

        public Builder services(Collection<Class<? extends Service>> services) {
            definition.services.addAll(services);
            return this;
        }

        public Builder messages(Collection<Class<? extends Message>> messages) {
            definition.messages.addAll(messages);
            return this;
        }

        public BoundedContextDefinition build() {
            return definition;
        }
    }

    private BoundedContextDefinition() {

    }

    private Set<EntityDefinition> definitions = new HashSet<>();

    public Set<EntityDefinition> getEntityDefinitions() {
        return unmodifiableSet(definitions);
    }

    private Set<Class<? extends DomainProcess>> processes = new HashSet<>();

    public Set<Class<? extends DomainProcess>> getProcesses() {
        return unmodifiableSet(processes);
    }

    private Set<Class<? extends Service>> services = new HashSet<>();

    public Set<Class<? extends Service>> getServices() {
        return unmodifiableSet(services);
    }

    private Set<Class<? extends Message>> messages = new HashSet<>();

    public Set<Class<? extends Message>> getMessages() {
        return unmodifiableSet(messages);
    }
}
