package poussecafe.doc.model.factorydoc;

import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.domain.EntityDataAccess;

public interface FactoryDocDataAccess<D extends FactoryDoc.Data> extends EntityDataAccess<FactoryDocKey, D> {

    List<D> findByBoundedContextKey(BoundedContextDocKey key);

    D findByBoundedContextKeyAndName(BoundedContextDocKey boundedContextDocKey,
            String factoryName);
}
