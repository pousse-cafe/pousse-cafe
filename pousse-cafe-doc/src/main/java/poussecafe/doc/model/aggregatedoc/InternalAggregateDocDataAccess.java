package poussecafe.doc.model.aggregatedoc;

import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.storage.internal.InternalDataAccess;

import static java.util.stream.Collectors.toList;

public class InternalAggregateDocDataAccess extends InternalDataAccess<AggregateDocKey, AggregateDocData> implements AggregateDocDataAccess<AggregateDocData> {

    @Override
    public List<AggregateDocData> findByBoundedContextKey(BoundedContextDocKey key) {
        return findAll().stream().filter(data -> data.boundedContextComponentDoc().get().boundedContextDocKey().equals(key)).collect(toList());
    }

    @Override
    public List<AggregateDocData> findByKeyClassName(String qualifiedName) {
        return findAll().stream().filter(data -> data.keyClassName().get().equals(qualifiedName)).collect(toList());
    }

    @Override
    public AggregateDocData findByBoundedContextKeyAndName(BoundedContextDocKey boundedContextDocKey,
            String aggregateName) {
        return findAll().stream()
                .filter(data -> data.boundedContextComponentDoc().get().boundedContextDocKey().equals(boundedContextDocKey))
                .filter(data -> data.boundedContextComponentDoc().get().componentDoc().name().equals(aggregateName))
                .findFirst().orElse(null);
    }

}
