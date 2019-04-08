package poussecafe.doc.model.boundedcontextdoc;

import poussecafe.discovery.DataAccessImplementation;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

@DataAccessImplementation(
    aggregateRoot = BoundedContextDoc.class,
    dataImplementation = BoundedContextDocData.class,
    storageName = InternalStorage.NAME
)
public class InternalBoundedContextDocDataAccess extends InternalDataAccess<BoundedContextDocId, BoundedContextDocData> implements BoundedContextDocDataAccess<BoundedContextDocData> {

    @Override
    public BoundedContextDocData findByPackageNamePrefixing(String packageName) {
        return findAll()
                .stream()
                .filter(data -> packageName.startsWith(data.identifier().value().stringValue()))
                .findFirst()
                .orElse(null);
    }

}
