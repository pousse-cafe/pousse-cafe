package poussecafe.doc.model.entitydoc;

import java.util.List;
import poussecafe.doc.model.aggregatedoc.AggregateDocKey;
import poussecafe.storable.IdentifiedStorableDataAccess;

public interface EntityDocDataAccess<D extends EntityDoc.Data> extends IdentifiedStorableDataAccess<EntityDocKey, D> {

    List<D> findByAggregateDocKey(AggregateDocKey key);

}
