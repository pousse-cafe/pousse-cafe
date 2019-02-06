package poussecafe.doc.model.domainprocessdoc;

import java.util.List;
import poussecafe.contextconfigurer.DataAccessImplementation;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

import static java.util.stream.Collectors.toList;

@DataAccessImplementation(
    aggregateRoot = DomainProcessDoc.class,
    dataImplementation = DomainProcessDocData.class,
    storageName = InternalStorage.NAME
)
public class InternalDomainProcessDocDataAccess extends InternalDataAccess<DomainProcessDocKey, DomainProcessDocData> implements DomainProcessDocDataAccess<DomainProcessDocData> {

    @Override
    public List<DomainProcessDocData> findByBoundedContextKey(BoundedContextDocKey key) {
        return findAll().stream().filter(data -> data.boundedContextComponentDoc().value().boundedContextDocKey().equals(key)).collect(toList());
    }
}
