package poussecafe.doc.model.servicedoc;

import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.storage.DataAccessImplementation;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

import static java.util.stream.Collectors.toList;

@DataAccessImplementation(
    aggregateRoot = ServiceDoc.class,
    dataImplementation = ServiceDocData.class,
    storageName = InternalStorage.NAME
)
public class InternalServiceDocDataAccess extends InternalDataAccess<ServiceDocKey, ServiceDocData> implements ServiceDocDataAccess<ServiceDocData> {

    @Override
    public List<ServiceDocData> findByBoundedContextKey(BoundedContextDocKey key) {
        return findAll().stream().filter(data -> data.boundedContextComponentDoc().get().boundedContextDocKey().equals(key)).collect(toList());
    }

}