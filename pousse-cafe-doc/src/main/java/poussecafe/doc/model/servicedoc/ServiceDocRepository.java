package poussecafe.doc.model.servicedoc;

import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.domain.Repository;

public class ServiceDocRepository extends Repository<ServiceDoc, ServiceDocKey, ServiceDoc.Data> {

    public List<ServiceDoc> findByBoundedContextKey(BoundedContextDocKey key) {
        return newEntitiesWithData(dataAccess().findByBoundedContextKey(key));
    }

    private ServiceDocDataAccess<ServiceDoc.Data> dataAccess() {
        return (ServiceDocDataAccess<ServiceDoc.Data>) dataAccess;
    }

    public List<ServiceDoc> findAll() {
        return newEntitiesWithData(dataAccess().findAll());
    }
}
