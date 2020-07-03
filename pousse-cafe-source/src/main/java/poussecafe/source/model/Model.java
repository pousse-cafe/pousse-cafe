package poussecafe.source.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

public class Model {

    public void addAggregateRoot(AggregateRootSource source) {
        String name = source.name();
        if(aggregateRoots.containsKey(name)) {
            throw new IllegalArgumentException("An aggregate root named " + name + " already exists in file " + source.filePath());
        } else {
            aggregateRoots.put(name, source);
        }
    }

    private Map<String, AggregateRootSource> aggregateRoots = new HashMap<>();

    public Optional<AggregateRootSource> aggregateRoot(String name) {
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

    public void addMessageListener(MessageListenerSource source) {
        listeners.add(source);
    }

    private List<MessageListenerSource> listeners = new ArrayList<>();

    public List<MessageListenerSource> aggregateRootListeners(String aggregateName) {
        return listeners.stream()
                .filter(listener -> listener.container().type() == MessageListenerContainerType.ROOT)
                .filter(listener -> listener.container().aggregateName().orElseThrow().equals(aggregateName))
                .collect(toList());
    }

    public List<MessageListenerSource> processListeners(String process) {
        return listeners.stream()
                .filter(listener -> listener.processNames().contains(process))
                .collect(toList());
    }

    public List<MessageListenerSource> messageListeners() {
        return unmodifiableList(listeners);
    }
}
