package poussecafe.source.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

public class ModelBuilder {

    // TODO remove
    public ModelBuilder putAggregate(Aggregate.Builder source) {
        aggregates.put(source.name().orElseThrow(), source);
        return this;
    }

    private Map<String, Aggregate.Builder> aggregates = new HashMap<>();

    public void addStandaloneAggregateRoot(StandaloneAggregateRoot root) {
        standaloneAggregateRoots.put(root.aggregateName(), root);
        standaloneAggregateRootsBySourceId.put(root.typeComponent().source().id(), root);
    }

    private Map<String, StandaloneAggregateRoot> standaloneAggregateRoots = new HashMap<>();

    private Map<String, StandaloneAggregateRoot> standaloneAggregateRootsBySourceId = new HashMap<>();

    public void addStandaloneAggregateFactory(StandaloneAggregateFactory factory) {
        standaloneAggregateFactories.put(factory.aggregateName(), factory);
        standaloneAggregateFactoriesBySourceId.put(factory.typeComponent().source().id(), factory);
    }

    private Map<String, StandaloneAggregateFactory> standaloneAggregateFactories = new HashMap<>();

    private Map<String, StandaloneAggregateFactory> standaloneAggregateFactoriesBySourceId = new HashMap<>();

    public void addStandaloneAggregateRepository(StandaloneAggregateRepository repository) {
        standaloneAggregateRepositories.put(repository.aggregateName(), repository);
        standaloneAggregateRepositoriesBySourceId.put(repository.typeComponent().source().id(), repository);
    }

    private Map<String, StandaloneAggregateRepository> standaloneAggregateRepositories = new HashMap<>();

    private Map<String, StandaloneAggregateRepository> standaloneAggregateRepositoriesBySourceId = new HashMap<>();

    public void addAggregateContainer(AggregateContainer container) {
        aggregateContainers.put(container.aggregateName(), container);
        aggregateContainersBySourceId.put(container.typeComponent().source().id(), container);
    }

    private Map<String, AggregateContainer> aggregateContainers = new HashMap<>();

    private Map<String, AggregateContainer> aggregateContainersBySourceId = new HashMap<>();

    public void addProcess(ProcessModel source) {
        String name = source.simpleName();
        processes.computeIfAbsent(name, key -> source);
    }

    private Map<String, ProcessModel> processes = new HashMap<>();

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
        standaloneAggregateFactoriesBySourceId.remove(sourceId);
        standaloneAggregateRootsBySourceId.remove(sourceId);
        standaloneAggregateRepositoriesBySourceId.remove(sourceId);
        aggregateContainersBySourceId.remove(sourceId);

        // TODO Auto-generated method stub
    }

    public Model build() {
        var model = new Model();
        processes.values().forEach(model::addProcess);
        commands.values().forEach(model::addCommand);
        events.values().forEach(model::addEvent);
        buildModelAggregates(model);
        listeners.forEach(model::addMessageListener);
        runners.values().forEach(model::addRunner);
        return model;
    }

    private void buildModelAggregates(Model model) {
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
        for(StandaloneAggregateFactory factory : standaloneAggregateFactories.values()) {
            var aggregate = aggregates.computeIfAbsent(factory.aggregateName(),
                    name -> newBuilder(name, factory.typeComponent().typeName().rootClassName().qualifier()));
            aggregate.innerFactory(false);
        }

        for(StandaloneAggregateRoot root : standaloneAggregateRoots.values()) {
            var aggregate = aggregates.computeIfAbsent(root.aggregateName(),
                    name -> newBuilder(name, root.typeComponent().typeName().rootClassName().qualifier()));
            aggregate.innerRoot(false);
            aggregate.onAddProducedEvents(root.hooks().onAddProducedEvents());
            aggregate.onDeleteProducedEvents(root.hooks().onDeleteProducedEvents());
        }

        for(StandaloneAggregateRepository repository : standaloneAggregateRepositories.values()) {
            var aggregate = aggregates.computeIfAbsent(repository.aggregateName(),
                    name -> newBuilder(name, repository.typeComponent().typeName().rootClassName().qualifier()));
            aggregate.innerRepository(false);
        }

        for(AggregateContainer container : aggregateContainers.values()) {
            var aggregate = aggregates.computeIfAbsent(container.aggregateName(),
                    name -> newBuilder(name, container.typeComponent().typeName().rootClassName().qualifier()));
            aggregate.onAddProducedEvents(container.hooks().onAddProducedEvents());
            aggregate.onDeleteProducedEvents(container.hooks().onDeleteProducedEvents());
        }
    }

    private Aggregate.Builder newBuilder(String name, String packageName) {
        var aggregateBuilder = new Aggregate.Builder();
        aggregateBuilder.name(name);
        aggregateBuilder.packageName(packageName);
        return aggregateBuilder;
    }
}
