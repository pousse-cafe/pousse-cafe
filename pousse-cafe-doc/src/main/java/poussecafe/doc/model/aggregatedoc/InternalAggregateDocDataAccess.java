package poussecafe.doc.model.aggregatedoc;

import java.util.List;
import poussecafe.discovery.DataAccessImplementation;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

import static java.util.stream.Collectors.toList;

@DataAccessImplementation(
    aggregateRoot = AggregateDoc.class,
    dataImplementation = AggregateDocData.class,
    storageName = InternalStorage.NAME
)
public class InternalAggregateDocDataAccess extends InternalDataAccess<AggregateDocKey, AggregateDocData> implements AggregateDocDataAccess<AggregateDocData> {

    @Override
    public List<AggregateDocData> findByBoundedContextKey(BoundedContextDocKey key) {
        return findAll().stream().filter(data -> data.boundedContextComponentDoc().value().boundedContextDocKey().equals(key)).collect(toList());
    }

    @Override
    public List<AggregateDocData> findByKeyClassName(String qualifiedName) {
        return findAll().stream().filter(data -> data.keyClassName().value().equals(qualifiedName)).collect(toList());
    }

    @Override
    public AggregateDocData findByBoundedContextKeyAndName(BoundedContextDocKey boundedContextDocKey,
            String aggregateName) {
        return findAll().stream()
                .filter(data -> data.boundedContextComponentDoc().value().boundedContextDocKey().equals(boundedContextDocKey))
                .filter(data -> data.boundedContextComponentDoc().value().componentDoc().name().equals(aggregateName))
                .findFirst().orElse(null);
    }

}
