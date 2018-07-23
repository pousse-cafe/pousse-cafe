package poussecafe.doc.model.boundedcontextdoc;

import poussecafe.storage.memory.InMemoryDataAccess;

public class InMemoryBoundedContextDocDataAccess extends InMemoryDataAccess<BoundedContextDocKey, BoundedContextDocData> implements BoundedContextDocDataAccess<BoundedContextDocData> {

    @Override
    public BoundedContextDocData findByPackageNamePrefixing(String packageName) {
        return findAll()
                .stream()
                .filter(data -> packageName.startsWith(data.packageName().get()))
                .findFirst()
                .orElse(null);
    }

}
