package poussecafe.source.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

public class Model {

    public void putAggregateRoot(Aggregate source) {
        aggregateRoots.put(source.name(), source);
    }

    private Map<String, Aggregate> aggregateRoots = new HashMap<>();

    public Optional<Aggregate> aggregateRoot(String name) {
        return Optional.ofNullable(aggregateRoots.get(name));
    }

    public Optional<ProcessModel> process(String name) {
        return Optional.ofNullable(processes.get(name));
    }

    private Map<String, ProcessModel> processes = new HashMap<>();

    public void addProcess(ProcessModel source) {
        String name = source.name();
        if(processes.containsKey(name)) {
            throw new IllegalArgumentException("A process named " + name + " already exists in file " + source.filePath());
        } else {
            processes.put(name, source);
        }
    }

    public void addMessageListener(MessageListener source) {
        listeners.add(source);
    }

    private List<MessageListener> listeners = new ArrayList<>();

    public List<MessageListener> aggregateRootListeners(String aggregateName) {
        return listeners.stream()
                .filter(listener -> listener.container().type() == MessageListenerContainerType.ROOT)
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
}
