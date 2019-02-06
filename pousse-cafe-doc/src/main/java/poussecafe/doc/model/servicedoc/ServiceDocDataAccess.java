package poussecafe.doc.model.servicedoc;

import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.domain.EntityDataAccess;

public interface ServiceDocDataAccess<D extends ServiceDoc.Attributes> extends EntityDataAccess<ServiceDocKey, D> {

    List<D> findByBoundedContextKey(BoundedContextDocKey key);

}
