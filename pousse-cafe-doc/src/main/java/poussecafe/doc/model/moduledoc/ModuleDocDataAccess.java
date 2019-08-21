package poussecafe.doc.model.moduledoc;

import poussecafe.domain.EntityDataAccess;

public interface ModuleDocDataAccess<D extends ModuleDoc.Attributes> extends EntityDataAccess<ModuleDocId, D> {

    D findByPackageNamePrefixing(String name);
}
