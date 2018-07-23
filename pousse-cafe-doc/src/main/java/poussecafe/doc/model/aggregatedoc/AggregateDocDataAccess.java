package poussecafe.doc.model.aggregatedoc;

import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.storable.IdentifiedStorableDataAccess;

public interface AggregateDocDataAccess<D extends AggregateDoc.Data> extends IdentifiedStorableDataAccess<AggregateDocKey, D> {

    List<D> findByBoundedContextKey(BoundedContextDocKey key);

    List<D> findByKeyClassName(String qualifiedName);

}
