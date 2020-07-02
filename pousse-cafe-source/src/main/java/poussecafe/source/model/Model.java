package poussecafe.source.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
}
