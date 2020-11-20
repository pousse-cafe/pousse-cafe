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

    public Aggregate.Builder getAndCreateIfAbsent(String name, String packageName) {
        return aggregates.computeIfAbsent(name, key -> newBuilder(key, packageName));
    }

    private Aggregate.Builder newBuilder(String name, String packageName) {
        var aggregateBuilder = new Aggregate.Builder();
        aggregateBuilder.name(name);
        aggregateBuilder.packageName(packageName);
        return aggregateBuilder;
    }

    public ModelBuilder putAggregate(Aggregate.Builder source) {
        aggregates.put(source.name().orElseThrow(), source);
        return this;
    }

    private Map<String, Aggregate.Builder> aggregates = new HashMap<>();

    public Optional<Aggregate.Builder> aggregate(String name) {
        return Optional.ofNullable(aggregates.get(name));
    }

    public void addProcess(ProcessModel source) {
        String name = source.simpleName();
        if(!processes.containsKey(name)) {
            processes.put(name, source);
        }
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

    public Model build() {
        var model = new Model();
        processes.values().forEach(model::addProcess);
        commands.values().forEach(model::addCommand);
        events.values().forEach(model::addEvent);
        setInnerClassFlags();
        aggregates.values().stream().map(Aggregate.Builder::build).forEach(model::addAggregate);
        listeners.forEach(model::addMessageListener);
        return model;
    }

    private void setInnerClassFlags() {
        for(MessageListener listener : listeners) {
            if(listener.isLinkedToAggregate()) {
                var aggregateName = listener.aggregateName();
                var builder = aggregates.get(aggregateName);
                if(builder == null) {
                    throw new IllegalStateException("Listener " + listener.methodName() + " refers to missing aggregate "
                            + aggregateName);
                }
                if(listener.container().type() == MessageListenerContainerType.FACTORY) {
                    builder.innerFactory(listener.container().isQualifiedIdentifier());
                } else if(listener.container().type() == MessageListenerContainerType.ROOT) {
                    builder.innerRoot(listener.container().isQualifiedIdentifier());
                } else if(listener.container().type() == MessageListenerContainerType.REPOSITORY) {
                    builder.innerRepository(listener.container().isQualifiedIdentifier());
                }
            }
        }
        for(Aggregate.Builder builder : aggregates.values()) {
            builder.ensureDefaultLocations();
        }
    }
}
