package poussecafe.doc.model.boundedcontextdoc;

import poussecafe.storable.IdentifiedStorableDataAccess;

public interface BoundedContextDocDataAccess<D extends BoundedContextDoc.Data> extends IdentifiedStorableDataAccess<BoundedContextDocKey, D> {

    D findByPackageNamePrefixing(String name);

}
