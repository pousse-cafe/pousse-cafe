package poussecafe.doc.model.boundedcontextdoc;

import poussecafe.storage.DataAccessImplementation;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

@DataAccessImplementation(
    aggregateRoot = BoundedContextDoc.class,
    dataImplementation = BoundedContextDocData.class,
    storageName = InternalStorage.NAME
)
public class InternalBoundedContextDocDataAccess extends InternalDataAccess<BoundedContextDocKey, BoundedContextDocData> implements BoundedContextDocDataAccess<BoundedContextDocData> {

    @Override
    public BoundedContextDocData findByPackageNamePrefixing(String packageName) {
        return findAll()
                .stream()
                .filter(data -> packageName.startsWith(data.key().get().getValue()))
                .findFirst()
                .orElse(null);
    }

}
