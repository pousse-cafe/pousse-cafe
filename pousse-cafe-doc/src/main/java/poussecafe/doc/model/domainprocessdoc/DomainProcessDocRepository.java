package poussecafe.doc.model.domainprocessdoc;

import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.domain.Repository;

public class DomainProcessDocRepository extends Repository<DomainProcessDoc, DomainProcessDocKey, DomainProcessDoc.Data> {

    public List<DomainProcessDoc> findAll() {
        return newStorablesWithData(dataAccess().findAll());
    }

    private DomainProcessDocDataAccess<DomainProcessDoc.Data> dataAccess() {
        return (DomainProcessDocDataAccess<DomainProcessDoc.Data>) dataAccess;
    }

    public List<DomainProcessDoc> findByBoundedContextKey(BoundedContextDocKey key) {
        return newStorablesWithData(dataAccess().findByBoundedContextKey(key));
    }
}
