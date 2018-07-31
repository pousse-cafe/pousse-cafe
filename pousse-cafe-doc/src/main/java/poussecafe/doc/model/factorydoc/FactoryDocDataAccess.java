package poussecafe.doc.model.factorydoc;

import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.storable.IdentifiedStorableDataAccess;

public interface FactoryDocDataAccess<D extends FactoryDoc.Data> extends IdentifiedStorableDataAccess<FactoryDocKey, D> {

    List<D> findByBoundedContextKey(BoundedContextDocKey key);

    D findByBoundedContextKeyAndName(BoundedContextDocKey boundedContextDocKey,
            String factoryName);
}
