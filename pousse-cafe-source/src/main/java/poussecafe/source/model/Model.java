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

public class Model {

    public void putAggregate(Aggregate source) {
        aggregates.put(source.name(), source);
    }

    private Map<String, Aggregate> aggregates = new HashMap<>();

    public Optional<Aggregate> aggregate(String name) {
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
        events.put(event.simpleName(), event);
    }

    private Map<String, DomainEvent> events = new HashMap<>();

    public Collection<DomainEvent> events() {
        return Collections.unmodifiableCollection(events.values());
    }

    public Optional<DomainEvent> event(String name) {
        return Optional.ofNullable(events.get(name));
    }

    public Collection<Aggregate> aggregates() {
        return Collections.unmodifiableCollection(aggregates.values());
    }
}
