package poussecafe.environment;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import poussecafe.domain.Service;
import poussecafe.messaging.Message;
import poussecafe.process.ExplicitDomainProcess;

import static java.util.Collections.unmodifiableSet;

public class BundleDefinition {

    public static class Builder {

        private BundleDefinition definition = new BundleDefinition();

        public Builder withAggregateDefinitions(Collection<AggregateDefinition> definitions) {
            definition.definitions.addAll(definitions);
            return this;
        }

        public Builder withAggregateDefinition(AggregateDefinition aggregateDefinition) {
            definition.definitions.add(aggregateDefinition);
            return this;
        }

        public Builder withDomainProcesses(Collection<Class<? extends ExplicitDomainProcess>> processes) {
            definition.processes.addAll(processes);
            return this;
        }

        public Builder withDomainProcess(Class<? extends ExplicitDomainProcess> domainClass) {
            definition.processes.add(domainClass);
            return this;
        }

        public Builder withServices(Collection<Class<? extends Service>> services) {
            definition.services.addAll(services);
            return this;
        }

        public Builder withMessages(Collection<Class<? extends Message>> messages) {
            definition.messages.addAll(messages);
            return this;
        }

        public Builder withMessage(Class<? extends Message> messageClass) {
            definition.messages.add(messageClass);
            return this;
        }

        public Builder withMessageListeners(Collection<MessageListenerDefinition> listeners) {
            definition.listeners.addAll(listeners);
            return this;
        }

        public Builder withMessageListener(MessageListenerDefinition listenerDefinition) {
            definition.listeners.add(listenerDefinition);
            return this;
        }

        public BundleDefinition build() {
            return definition;
        }
    }

    private BundleDefinition() {

    }

    private Set<AggregateDefinition> definitions = new HashSet<>();

    public Set<AggregateDefinition> definedAggregates() {
        return unmodifiableSet(definitions);
    }

    private Set<Class<? extends ExplicitDomainProcess>> processes = new HashSet<>();

    public Set<Class<? extends ExplicitDomainProcess>> definedDomainProcesses() {
        return unmodifiableSet(processes);
    }

    private Set<Class<? extends Service>> services = new HashSet<>();

    public Set<Class<? extends Service>> definedServices() {
        return unmodifiableSet(services);
    }

    private Set<Class<? extends Message>> messages = new HashSet<>();

    public Set<Class<? extends Message>> definedMessages() {
        return unmodifiableSet(messages);
    }

    private Set<MessageListenerDefinition> listeners = new HashSet<>();

    public Set<MessageListenerDefinition> definedListeners() {
        return unmodifiableSet(listeners);
    }
}
