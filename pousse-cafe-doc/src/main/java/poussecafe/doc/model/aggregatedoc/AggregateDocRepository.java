package poussecafe.doc.model.aggregatedoc;

import java.util.List;
import poussecafe.domain.Repository;

public class AggregateDocRepository extends Repository<AggregateDoc, AggregateDocKey, AggregateDoc.Data> {

    public List<AggregateDoc> findByBoundedContextKey(String key) {
        return newStorablesWithData(dataAccess().findByBoundedContextKey(key));
    }

    private AggregateDocDataAccess<AggregateDoc.Data> dataAccess() {
        return (AggregateDocDataAccess<AggregateDoc.Data>) dataAccess;
    }
}
