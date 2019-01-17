package poussecafe.doc.model.domainprocessdoc;

import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.domain.Repository;

public class DomainProcessDocRepository extends Repository<DomainProcessDoc, DomainProcessDocKey, DomainProcessDoc.Data> {

    public List<DomainProcessDoc> findAll() {
        return wrap(dataAccess().findAll());
    }

    @Override
    public DomainProcessDocDataAccess<DomainProcessDoc.Data> dataAccess() {
        return (DomainProcessDocDataAccess<DomainProcessDoc.Data>) super.dataAccess();
    }

    public List<DomainProcessDoc> findByBoundedContextKey(BoundedContextDocKey key) {
        return wrap(dataAccess().findByBoundedContextKey(key));
    }
}
