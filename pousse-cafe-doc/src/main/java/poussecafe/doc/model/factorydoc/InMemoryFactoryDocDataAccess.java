package poussecafe.doc.model.factorydoc;

import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.storage.memory.InMemoryDataAccess;

import static java.util.stream.Collectors.toList;

public class InMemoryFactoryDocDataAccess extends InMemoryDataAccess<FactoryDocKey, FactoryDocData> implements FactoryDocDataAccess<FactoryDocData> {

    @Override
    public List<FactoryDocData> findByBoundedContextKey(BoundedContextDocKey key) {
        return findAll().stream().filter(data -> data.boundedContextComponentDoc().get().boundedContextDocKey().equals(key)).collect(toList());
    }

    @Override
    public FactoryDocData findByBoundedContextKeyAndName(BoundedContextDocKey boundedContextDocKey,
            String aggregateName) {
        return findAll().stream()
                .filter(data -> data.boundedContextComponentDoc().get().boundedContextDocKey().equals(boundedContextDocKey))
                .filter(data -> data.boundedContextComponentDoc().get().componentDoc().name().equals(aggregateName))
                .findFirst().orElse(null);
    }
}
