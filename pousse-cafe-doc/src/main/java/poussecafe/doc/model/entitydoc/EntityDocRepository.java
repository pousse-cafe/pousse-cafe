package poussecafe.doc.model.entitydoc;

import java.util.List;
import poussecafe.doc.model.aggregatedoc.AggregateDocKey;
import poussecafe.domain.Repository;

public class EntityDocRepository extends Repository<EntityDoc, EntityDocKey, EntityDoc.Data> {

    public List<EntityDoc> findByAggregateDocKey(AggregateDocKey key) {
        return newStorablesWithData(dataAccess().findByAggregateDocKey(key));
    }

    private EntityDocDataAccess<EntityDoc.Data> dataAccess() {
        return (EntityDocDataAccess<EntityDoc.Data>) dataAccess;
    }

    public List<EntityDoc> findAll() {
        return newStorablesWithData(dataAccess().findAll());
    }
}
