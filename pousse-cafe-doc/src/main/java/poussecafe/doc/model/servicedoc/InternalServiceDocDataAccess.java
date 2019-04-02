package poussecafe.doc.model.servicedoc;

import java.util.List;
import poussecafe.discovery.DataAccessImplementation;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

import static java.util.stream.Collectors.toList;

@DataAccessImplementation(
    aggregateRoot = ServiceDoc.class,
    dataImplementation = ServiceDocData.class,
    storageName = InternalStorage.NAME
)
public class InternalServiceDocDataAccess extends InternalDataAccess<ServiceDocId, ServiceDocData> implements ServiceDocDataAccess<ServiceDocData> {

    @Override
    public List<ServiceDocData> findByBoundedContextId(BoundedContextDocId id) {
        return findAll().stream().filter(data -> data.boundedContextComponentDoc().value().boundedContextDocId().equals(id)).collect(toList());
    }

}
