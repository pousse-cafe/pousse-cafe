package poussecafe.doc.model.factorydoc;

import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.domain.Repository;

public class FactoryDocRepository extends Repository<FactoryDoc, FactoryDocKey, FactoryDoc.Data> {

    public List<FactoryDoc> findByBoundedContextKey(BoundedContextDocKey key) {
        return wrap(dataAccess().findByBoundedContextKey(key));
    }

    @Override
    public FactoryDocDataAccess<FactoryDoc.Data> dataAccess() {
        return (FactoryDocDataAccess<FactoryDoc.Data>) super.dataAccess();
    }

    public List<FactoryDoc> findAll() {
        return wrap(dataAccess().findAll());
    }

    public FactoryDoc findByBoundedContextKeyAndName(BoundedContextDocKey boundedContextDocKey,
            String factoryName) {
        return wrap(dataAccess().findByBoundedContextKeyAndName(boundedContextDocKey, factoryName));
    }
}
