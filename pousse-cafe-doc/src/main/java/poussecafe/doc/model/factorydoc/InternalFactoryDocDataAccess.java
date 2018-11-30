package poussecafe.doc.model.factorydoc;

import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.storage.DataAccessImplementation;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

import static java.util.stream.Collectors.toList;

@DataAccessImplementation(
    aggregateRoot = FactoryDoc.class,
    dataImplementation = FactoryDocData.class,
    storageName = InternalStorage.NAME
)
public class InternalFactoryDocDataAccess extends InternalDataAccess<FactoryDocKey, FactoryDocData> implements FactoryDocDataAccess<FactoryDocData> {

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