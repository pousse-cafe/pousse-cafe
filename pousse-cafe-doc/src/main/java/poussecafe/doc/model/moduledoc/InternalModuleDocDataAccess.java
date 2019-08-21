package poussecafe.doc.model.moduledoc;

import poussecafe.discovery.DataAccessImplementation;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

@DataAccessImplementation(
    aggregateRoot = ModuleDoc.class,
    dataImplementation = ModuleDocData.class,
    storageName = InternalStorage.NAME
)
public class InternalModuleDocDataAccess extends InternalDataAccess<ModuleDocId, ModuleDocData> implements ModuleDocDataAccess<ModuleDocData> {

    @Override
    public ModuleDocData findByPackageNamePrefixing(String packageName) {
        return findAll()
                .stream()
                .filter(data -> packageName.startsWith(data.identifier().value().stringValue()))
                .findFirst()
                .orElse(null);
    }

}
