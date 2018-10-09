package poussecafe.doc.model.domainprocessdoc;

import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.domain.EntityDataAccess;

public interface DomainProcessDocDataAccess<D extends DomainProcessDoc.Data> extends EntityDataAccess<DomainProcessDocKey, D> {

    List<D> findByBoundedContextKey(BoundedContextDocKey key);
}
