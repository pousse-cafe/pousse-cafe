package poussecafe.doc.model.boundedcontextdoc;

import poussecafe.storage.internal.InternalDataAccess;

public class InternalBoundedContextDocDataAccess extends InternalDataAccess<BoundedContextDocKey, BoundedContextDocData> implements BoundedContextDocDataAccess<BoundedContextDocData> {

    @Override
    public BoundedContextDocData findByPackageNamePrefixing(String packageName) {
        return findAll()
                .stream()
                .filter(data -> packageName.startsWith(data.packageName().get()))
                .findFirst()
                .orElse(null);
    }

}
