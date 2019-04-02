package poussecafe.doc.model.factorydoc;

import java.util.List;
import poussecafe.discovery.DataAccessImplementation;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

import static java.util.stream.Collectors.toList;

@DataAccessImplementation(
    aggregateRoot = FactoryDoc.class,
    dataImplementation = FactoryDocData.class,
    storageName = InternalStorage.NAME
)
public class InternalFactoryDocDataAccess extends InternalDataAccess<FactoryDocId, FactoryDocData> implements FactoryDocDataAccess<FactoryDocData> {

    @Override
    public List<FactoryDocData> findByBoundedContextId(BoundedContextDocId id) {
        return findAll().stream().filter(data -> data.boundedContextComponentDoc().value().boundedContextDocId().equals(id)).collect(toList());
    }

    @Override
    public FactoryDocData findByBoundedContextIdAndName(BoundedContextDocId boundedContextDocId,
            String aggregateName) {
        return findAll().stream()
                .filter(data -> data.boundedContextComponentDoc().value().boundedContextDocId().equals(boundedContextDocId))
                .filter(data -> data.boundedContextComponentDoc().value().componentDoc().name().equals(aggregateName))
                .findFirst().orElse(null);
    }
}
