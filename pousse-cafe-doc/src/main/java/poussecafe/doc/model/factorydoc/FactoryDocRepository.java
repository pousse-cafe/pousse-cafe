package poussecafe.doc.model.factorydoc;

import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.domain.Repository;

public class FactoryDocRepository extends Repository<FactoryDoc, FactoryDocId, FactoryDoc.Attributes> {

    public List<FactoryDoc> findByBoundedContextId(BoundedContextDocId id) {
        return wrap(dataAccess().findByBoundedContextId(id));
    }

    @Override
    public FactoryDocDataAccess<FactoryDoc.Attributes> dataAccess() {
        return (FactoryDocDataAccess<FactoryDoc.Attributes>) super.dataAccess();
    }

    public List<FactoryDoc> findAll() {
        return wrap(dataAccess().findAll());
    }

    public FactoryDoc findByBoundedContextIdAndName(BoundedContextDocId boundedContextDocId,
            String factoryName) {
        return wrap(dataAccess().findByBoundedContextIdAndName(boundedContextDocId, factoryName));
    }
}
