package poussecafe.doc.model.servicedoc;

import java.util.List;
import poussecafe.discovery.DataAccessImplementation;
import poussecafe.doc.model.moduledoc.ModuleDocId;
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
    public List<ServiceDocData> findByModuleDocId(ModuleDocId id) {
        return findAll().stream().filter(data -> data.moduleComponentDoc().value().moduleDocId().equals(id)).collect(toList());
    }

}
