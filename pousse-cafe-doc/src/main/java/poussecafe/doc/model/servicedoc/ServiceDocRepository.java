package poussecafe.doc.model.servicedoc;

import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.domain.Repository;

public class ServiceDocRepository extends Repository<ServiceDoc, ServiceDocKey, ServiceDoc.Attributes> {

    public List<ServiceDoc> findByBoundedContextKey(BoundedContextDocKey key) {
        return wrap(dataAccess().findByBoundedContextKey(key));
    }

    @Override
    public ServiceDocDataAccess<ServiceDoc.Attributes> dataAccess() {
        return (ServiceDocDataAccess<ServiceDoc.Attributes>) super.dataAccess();
    }

    public List<ServiceDoc> findAll() {
        return wrap(dataAccess().findAll());
    }
}
