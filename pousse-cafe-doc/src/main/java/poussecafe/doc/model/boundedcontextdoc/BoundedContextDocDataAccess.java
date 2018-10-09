package poussecafe.doc.model.boundedcontextdoc;

import poussecafe.domain.EntityDataAccess;

public interface BoundedContextDocDataAccess<D extends BoundedContextDoc.Data> extends EntityDataAccess<BoundedContextDocKey, D> {

    D findByPackageNamePrefixing(String name);

}
