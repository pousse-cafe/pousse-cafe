package poussecafe.doc.model.domainprocessdoc;

import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.storable.IdentifiedStorableDataAccess;

public interface DomainProcessDocDataAccess<D extends DomainProcessDoc.Data> extends IdentifiedStorableDataAccess<DomainProcessDocKey, D> {

    List<D> findByBoundedContextKey(BoundedContextDocKey key);
}
