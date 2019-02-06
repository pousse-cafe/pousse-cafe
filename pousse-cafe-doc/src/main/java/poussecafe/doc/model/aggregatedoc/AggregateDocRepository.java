package poussecafe.doc.model.aggregatedoc;

import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.domain.Repository;

public class AggregateDocRepository extends Repository<AggregateDoc, AggregateDocKey, AggregateDoc.Attributes> {

    public List<AggregateDoc> findByBoundedContextKey(BoundedContextDocKey key) {
        return wrap(dataAccess().findByBoundedContextKey(key));
    }

    @Override
    public AggregateDocDataAccess<AggregateDoc.Attributes> dataAccess() {
        return (AggregateDocDataAccess<AggregateDoc.Attributes>) super.dataAccess();
    }

    public List<AggregateDoc> findAll() {
        return wrap(dataAccess().findAll());
    }

    public List<AggregateDoc> findByKeyClassName(String qualifiedName) {
        return wrap(dataAccess().findByKeyClassName(qualifiedName));
    }

    public AggregateDoc findByBoundedContextKeyAndName(BoundedContextDocKey boundedContextDocKey,
            String aggregateName) {
        return wrap(dataAccess().findByBoundedContextKeyAndName(boundedContextDocKey, aggregateName));
    }
}
