package poussecafe.spring;

import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import poussecafe.configuration.ActiveStorableConfiguration;
import poussecafe.configuration.AggregateConfiguration;
import poussecafe.configuration.ApplicationConfiguration;
import poussecafe.configuration.ProcessManagerConfiguration;
import poussecafe.consequence.ConsequenceEmitter;
import poussecafe.consequence.ConsequenceReceiver;
import poussecafe.service.Workflow;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

public abstract class SpringApplicationConfiguration extends ApplicationConfiguration {

    @Autowired(required = false)
    private Set<AggregateConfiguration<?, ?, ?, ?, ?>> aggregateConfigurations;

    @Autowired(required = false)
    private Set<ProcessManagerConfiguration<?, ?, ?, ?, ?>> processManagerConfigurations;

    @Autowired(required = false)
    private Set<Workflow> workflows;

    @Autowired(required = false)
    private Set<ConsequenceEmitter> emitters;

    @Autowired(required = false)
    private Set<ConsequenceReceiver> receivers;

    @Override
    public Set<ActiveStorableConfiguration<?, ?, ?, ?, ?>> aggregateConfigurations() {
        return Optional
                .ofNullable(aggregateConfigurations)
                .orElse(emptySet())
                .stream()
                .map(config -> (ActiveStorableConfiguration<?, ?, ?, ?, ?>) config)
                .collect(toSet());
    }

    @Override
    public Set<ActiveStorableConfiguration<?, ?, ?, ?, ?>> processManagerConfigurations() {
        return Optional
                .ofNullable(processManagerConfigurations)
                .orElse(emptySet())
                .stream()
                .map(config -> (ActiveStorableConfiguration<?, ?, ?, ?, ?>) config)
                .collect(toSet());
    }

    @Override
    public Set<Workflow> workflows() {
        return Optional.ofNullable(workflows).orElse(emptySet());
    }

    @Override
    public Set<ConsequenceEmitter> consequenceEmitters() {
        return Optional.ofNullable(emitters).orElse(super.consequenceEmitters());
    }

    @Override
    public Set<ConsequenceReceiver> consequenceReceivers() {
        return Optional.ofNullable(receivers).orElse(super.consequenceReceivers());
    }
}
