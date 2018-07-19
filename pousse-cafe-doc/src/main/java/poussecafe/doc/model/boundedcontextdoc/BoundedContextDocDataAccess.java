package poussecafe.doc.model.boundedcontextdoc;

import poussecafe.storable.IdentifiedStorableDataAccess;

public interface BoundedContextDocDataAccess<D extends BoundedContextDoc.Data> extends IdentifiedStorableDataAccess<String, D> {

    D findByPackageNamePrefixing(String name);

}
