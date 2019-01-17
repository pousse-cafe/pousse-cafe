package poussecafe.doc.model.aggregatedoc;

import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.domain.Repository;

public class AggregateDocRepository extends Repository<AggregateDoc, AggregateDocKey, AggregateDoc.Data> {

    public List<AggregateDoc> findByBoundedContextKey(BoundedContextDocKey key) {
        return newEntitiesWithData(dataAccess().findByBoundedContextKey(key));
    }

    @Override
    public AggregateDocDataAccess<AggregateDoc.Data> dataAccess() {
        return (AggregateDocDataAccess<AggregateDoc.Data>) super.dataAccess();
    }

    public List<AggregateDoc> findAll() {
        return newEntitiesWithData(dataAccess().findAll());
    }

    public List<AggregateDoc> findByKeyClassName(String qualifiedName) {
        return newEntitiesWithData(dataAccess().findByKeyClassName(qualifiedName));
    }

    public AggregateDoc findByBoundedContextKeyAndName(BoundedContextDocKey boundedContextDocKey,
            String aggregateName) {
        return newEntityWithData(dataAccess().findByBoundedContextKeyAndName(boundedContextDocKey, aggregateName));
    }
}
