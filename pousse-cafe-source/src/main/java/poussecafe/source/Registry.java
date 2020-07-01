package poussecafe.source;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Registry {

    public void registerAggregateRoot(AggregateRootSource source) {
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
}
