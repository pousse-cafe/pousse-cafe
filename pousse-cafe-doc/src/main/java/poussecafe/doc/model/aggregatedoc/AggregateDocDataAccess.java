package poussecafe.doc.model.aggregatedoc;

import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.domain.EntityDataAccess;

public interface AggregateDocDataAccess<D extends AggregateDoc.Attributes> extends EntityDataAccess<AggregateDocKey, D> {

    List<D> findByBoundedContextKey(BoundedContextDocKey key);

    List<D> findByKeyClassName(String qualifiedName);

    D findByBoundedContextKeyAndName(BoundedContextDocKey boundedContextDocKey,
            String aggregateName);

}
