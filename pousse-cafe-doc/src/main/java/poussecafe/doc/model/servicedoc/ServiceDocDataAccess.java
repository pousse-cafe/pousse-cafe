package poussecafe.doc.model.servicedoc;

import java.util.List;
import poussecafe.storable.IdentifiedStorableDataAccess;

public interface ServiceDocDataAccess<D extends ServiceDoc.Data> extends IdentifiedStorableDataAccess<ServiceDocKey, D> {

    List<D> findByBoundedContextKey(String key);

}
