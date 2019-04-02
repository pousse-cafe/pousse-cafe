package poussecafe.doc.model.aggregatedoc;

import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.domain.EntityDataAccess;

public interface AggregateDocDataAccess<D extends AggregateDoc.Attributes> extends EntityDataAccess<AggregateDocId, D> {

    List<D> findByBoundedContextId(BoundedContextDocId id);

    List<D> findByIdClassName(String qualifiedName);

    D findByBoundedContextIdAndName(BoundedContextDocId boundedContextDocId,
            String aggregateName);

}
