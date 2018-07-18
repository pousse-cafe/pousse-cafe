package poussecafe.doc.model;

import poussecafe.storable.IdentifiedStorableDataAccess;

public interface BoundedContextDocDataAccess<D extends BoundedContextDoc.Data> extends IdentifiedStorableDataAccess<String, D> {

}
