package poussecafe.doc.model.factorydoc;

import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.domain.Repository;

public class FactoryDocRepository extends Repository<FactoryDoc, FactoryDocKey, FactoryDoc.Data> {

    public List<FactoryDoc> findByBoundedContextKey(BoundedContextDocKey key) {
        return newEntitiesWithData(dataAccess().findByBoundedContextKey(key));
    }

    private FactoryDocDataAccess<FactoryDoc.Data> dataAccess() {
        return (FactoryDocDataAccess<FactoryDoc.Data>) dataAccess;
    }

    public List<FactoryDoc> findAll() {
        return newEntitiesWithData(dataAccess().findAll());
    }

    public FactoryDoc findByBoundedContextKeyAndName(BoundedContextDocKey boundedContextDocKey,
            String factoryName) {
        return newEntityWithData(dataAccess().findByBoundedContextKeyAndName(boundedContextDocKey, factoryName));
    }
}
