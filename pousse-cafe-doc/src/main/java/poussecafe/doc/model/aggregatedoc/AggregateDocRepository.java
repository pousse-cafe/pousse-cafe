package poussecafe.doc.model.aggregatedoc;

import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.domain.Repository;

public class AggregateDocRepository extends Repository<AggregateDoc, AggregateDocKey, AggregateDoc.Data> {

    public List<AggregateDoc> findByBoundedContextKey(BoundedContextDocKey key) {
        return newStorablesWithData(dataAccess().findByBoundedContextKey(key));
    }

    private AggregateDocDataAccess<AggregateDoc.Data> dataAccess() {
        return (AggregateDocDataAccess<AggregateDoc.Data>) dataAccess;
    }

    public List<AggregateDoc> findAll() {
        return newStorablesWithData(dataAccess().findAll());
    }

    public List<AggregateDoc> findByKeyClassName(String qualifiedName) {
        return newStorablesWithData(dataAccess().findByKeyClassName(qualifiedName));
    }

    public AggregateDoc findByBoundedContextKeyAndName(BoundedContextDocKey boundedContextDocKey,
            String aggregateName) {
        return newStorableWithData(dataAccess().findByBoundedContextKeyAndName(boundedContextDocKey, aggregateName));
    }
}
