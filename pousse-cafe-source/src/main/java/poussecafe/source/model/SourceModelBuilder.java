package poussecafe.source.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

@SuppressWarnings("serial")
public class SourceModelBuilder implements Serializable {

    public SourceModelBuilder putAggregate(Aggregate.Builder source) {
        source.provided(true);
        aggregates.put(source.name().orElseThrow(), source);
        return this;
    }

    private Map<String, Aggregate.Builder> aggregates = new HashMap<>();

    public void addStandaloneAggregateRoot(StandaloneAggregateRoot root) {
        standaloneAggregateRoots.put(root.aggregateName(), root);
    }

    private Map<String, StandaloneAggregateRoot> standaloneAggregateRoots = new HashMap<>();

    public void addStandaloneAggregateFactory(StandaloneAggregateFactory factory) {
        standaloneAggregateFactories.put(factory.aggregateName(), factory);
    }

    private Map<String, StandaloneAggregateFactory> standaloneAggregateFactories = new HashMap<>();

    public void addStandaloneAggregateRepository(StandaloneAggregateRepository repository) {
        standaloneAggregateRepositories.put(repository.aggregateName(), repository);
    }

    private Map<String, StandaloneAggregateRepository> standaloneAggregateRepositories = new HashMap<>();

    public void addAggregateContainer(AggregateContainer container) {
        aggregateContainers.put(container.aggregateName(), container);
    }

    private Map<String, AggregateContainer> aggregateContainers = new HashMap<>();

    public void addProcess(ProcessModel process) {
        String name = process.simpleName();
        processes.computeIfAbsent(name, key -> process);
        processesBySourceId.put(process.source().id(), process);
    }

    private Map<String, ProcessModel> processes = new HashMap<>();

    private Map<String, ProcessModel> processesBySourceId = new HashMap<>();

    public Optional<ProcessModel> process(String name) {
        return Optional.ofNullable(processes.get(name));
    }

    public Collection<ProcessModel> processes() {
        return Collections.unmodifiableCollection(processes.values());
    }

    public void addMessageListener(MessageListener source) {
        listeners.add(source);
    }

    private List<MessageListener> listeners = new ArrayList<>();

    public List<MessageListener> aggregateListeners(String aggregateName) {
        return listeners.stream()
                .filter(listener -> listener.container().aggregateName().isPresent())
                .filter(listener -> listener.container().aggregateName().orElseThrow().equals(aggregateName))
                .collect(toList());
    }

    public List<MessageListener> processListeners(String process) {
        return listeners.stream()
                .filter(listener -> listener.processNames().contains(process))
                .collect(toList());
    }

    public List<MessageListener> messageListeners() {
        return unmodifiableList(listeners);
    }

    public void addCommand(Command command) {
        var existingCommand = commands.get(command.simpleName());
        if(existingCommand != null
                && !existingCommand.name().equals(command.name())) {
            throw new IllegalArgumentException("A command with this name already exists but qualifiers do not match: "
                    + existingCommand.name().getQualifier() + " <> " + command.name().getQualifier());
        }
        commands.put(command.simpleName(), command);
    }

    private Map<String, Command> commands = new HashMap<>();

    public Collection<Command> commands() {
        return Collections.unmodifiableCollection(commands.values());
    }

    public Optional<Command> command(String name) {
        return Optional.ofNullable(commands.get(name));
    }

    public void addEvent(DomainEvent event) {
        var existingEvent = events.get(event.simpleName());
        if(existingEvent != null
                && !existingEvent.name().equals(event.name())) {
            throw new IllegalArgumentException("An event with this name already exists but qualifiers do not match: "
                    + existingEvent.name().getQualifier() + " <> " + event.name().getQualifier());
        }
        events.put(event.simpleName(), event);
    }

    private Map<String, DomainEvent> events = new HashMap<>();

    public Collection<DomainEvent> events() {
        return Collections.unmodifiableCollection(events.values());
    }

    public Optional<DomainEvent> event(String name) {
        return Optional.ofNullable(events.get(name));
    }

    public void addRunner(Runner runnerClass) {
        runners.put(runnerClass.className(), runnerClass);
    }

    private Map<String, Runner> runners = new HashMap<>();

    public void forget(String sourceId) {
        forget(sourceId, standaloneAggregateFactories.values());
        forget(sourceId, standaloneAggregateRoots.values());
        forget(sourceId, standaloneAggregateRepositories.values());
        forget(sourceId, aggregateContainers.values());
        forget(sourceId, processes.values());
        forget(sourceId, commands.values());
        forget(sourceId, events.values());
        forget(sourceId, runners.values());
        listeners.removeIf(listener -> listener.source().id().equals(sourceId));
    }

    private <T extends WithTypeComponent> void forget(
            String sourceId,
            Collection<T> components) {
        components.removeIf(component -> component.typeComponent().source().id().equals(sourceId));
    }

    public SourceModel build() {
        var model = new SourceModel();
        processes.values().forEach(model::addProcess);
        commands.values().forEach(model::addCommand);
        events.values().forEach(model::addEvent);
        buildModelAggregates(model);
        listeners.forEach(model::addMessageListener);
        runners.values().forEach(model::addRunner);
        return model;
    }

    private void buildModelAggregates(SourceModel model) {
        initAggregateBuilders();
        for(MessageListener listener : listeners) {
            if(listener.isLinkedToAggregate()) {
                var aggregateName = listener.aggregateName();
                var builder = aggregates.get(aggregateName);
                if(builder == null) {
                    throw new IllegalStateException("Listener " + listener.methodName() + " refers to missing aggregate "
                            + aggregateName);
                }
                if(listener.container().type().isFactory()) {
                    builder.innerFactory(listener.container().type() == MessageListenerContainerType.INNER_FACTORY);
                } else if(listener.container().type().isRoot()) {
                    builder.innerRoot(listener.container().type() == MessageListenerContainerType.INNER_ROOT);
                } else if(listener.container().type().isRepository()) {
                    builder.innerRepository(listener.container().type() == MessageListenerContainerType.INNER_REPOSITORY);
                }
            }
        }

        for(Aggregate.Builder builder : aggregates.values()) {
            builder.ensureDefaultLocations();
        }

        aggregates.values().stream().map(Aggregate.Builder::build).forEach(model::addAggregate);
    }

    private void initAggregateBuilders() {
        aggregates.entrySet().removeIf(entry -> !entry.getValue().provided());

        for(StandaloneAggregateFactory factory : standaloneAggregateFactories.values()) {
            var aggregate = aggregates.computeIfAbsent(factory.aggregateName(),
                    name -> newBuilder(name, factory.typeComponent().typeName().rootClassName().qualifier()));
            aggregate.innerFactory(false);
            aggregate.standaloneFactorySource(Optional.of(factory.typeComponent().source()));
        }

        for(StandaloneAggregateRoot root : standaloneAggregateRoots.values()) {
            var aggregate = aggregates.computeIfAbsent(root.aggregateName(),
                    name -> newBuilder(name, root.typeComponent().typeName().rootClassName().qualifier()));
            aggregate.innerRoot(false);
            aggregate.standaloneRootSource(Optional.of(root.typeComponent().source()));
        }

        for(StandaloneAggregateRepository repository : standaloneAggregateRepositories.values()) {
            var aggregate = aggregates.computeIfAbsent(repository.aggregateName(),
                    name -> newBuilder(name, repository.typeComponent().typeName().rootClassName().qualifier()));
            aggregate.standaloneRepositorySource(Optional.of(repository.typeComponent().source()));
            aggregate.innerRepository(false);
        }

        for(AggregateContainer container : aggregateContainers.values()) {
            var aggregate = aggregates.computeIfAbsent(container.aggregateName(),
                    name -> newBuilder(name, container.typeComponent().typeName().rootClassName().qualifier()));
            aggregate.containerSource(Optional.of(container.typeComponent().source()));
        }
    }

    private Aggregate.Builder newBuilder(String name, String packageName) {
        var aggregateBuilder = new Aggregate.Builder();
        aggregateBuilder.name(name);
        aggregateBuilder.packageName(packageName);
        return aggregateBuilder;
    }
}
