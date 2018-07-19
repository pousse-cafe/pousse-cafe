package poussecafe.doc.model.aggregatedoc;

import java.util.List;
import poussecafe.storage.memory.InMemoryDataAccess;

import static java.util.stream.Collectors.toList;

public class InMemoryAggregateDocDataAccess extends InMemoryDataAccess<AggregateDocKey, AggregateDocData> implements AggregateDocDataAccess<AggregateDocData> {

    @Override
    public List<AggregateDocData> findByBoundedContextKey(String key) {
        return findAll().stream().filter(data -> data.key().get().boundedContextKey().equals(key)).collect(toList());
    }

}
