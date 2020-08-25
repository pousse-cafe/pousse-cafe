package poussecafe.source.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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

    public Optional<ProcessModel> process(String name) {
        return Optional.ofNullable(processes.get(name));
    }

    private Map<String, ProcessModel> processes = new HashMap<>();

    public void addProcess(ProcessModel source) {
        String name = source.name();
        if(!processes.containsKey(name)) {
            processes.put(name, source);
        }
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

    public void addCommand(String commandName) {
        commands.add(commandName);
    }

    private Set<String> commands = new HashSet<>();

    public Set<String> commands() {
        return Collections.unmodifiableSet(commands);
    }

    public void addEvent(String eventName) {
        events.add(eventName);
    }

    private Set<String> events = new HashSet<>();

    public Set<String> events() {
        return Collections.unmodifiableSet(events);
    }
}
