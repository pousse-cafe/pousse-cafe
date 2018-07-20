package poussecafe.doc.model.entitydoc;

import java.util.List;
import poussecafe.doc.model.aggregatedoc.AggregateDocKey;
import poussecafe.storage.memory.InMemoryDataAccess;

import static java.util.stream.Collectors.toList;

public class InMemoryEntityDocDataAccess extends InMemoryDataAccess<EntityDocKey, EntityDocData> implements EntityDocDataAccess<EntityDocData> {

    @Override
    public List<EntityDocData> findByAggregateDocKey(AggregateDocKey key) {
        return findAll().stream().filter(data -> data.key().get().aggregateDocKey().equals(key)).collect(toList());
    }

}
