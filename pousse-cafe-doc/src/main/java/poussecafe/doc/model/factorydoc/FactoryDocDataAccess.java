package poussecafe.doc.model.factorydoc;

import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.domain.EntityDataAccess;

public interface FactoryDocDataAccess<D extends FactoryDoc.Attributes> extends EntityDataAccess<FactoryDocId, D> {

    List<D> findByBoundedContextId(BoundedContextDocId id);

    D findByBoundedContextIdAndName(BoundedContextDocId boundedContextDocId,
            String factoryName);
}
