package poussecafe.doc.model.domainprocessdoc;

import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.domain.EntityDataAccess;

public interface DomainProcessDocDataAccess<D extends DomainProcessDoc.Attributes> extends EntityDataAccess<DomainProcessDocId, D> {

    List<D> findByBoundedContextId(BoundedContextDocId id);
}
