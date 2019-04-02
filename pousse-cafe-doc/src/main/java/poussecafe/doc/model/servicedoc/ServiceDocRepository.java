package poussecafe.doc.model.servicedoc;

import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.domain.Repository;

public class ServiceDocRepository extends Repository<ServiceDoc, ServiceDocId, ServiceDoc.Attributes> {

    public List<ServiceDoc> findByBoundedContextId(BoundedContextDocId id) {
        return wrap(dataAccess().findByBoundedContextId(id));
    }

    @Override
    public ServiceDocDataAccess<ServiceDoc.Attributes> dataAccess() {
        return (ServiceDocDataAccess<ServiceDoc.Attributes>) super.dataAccess();
    }

    public List<ServiceDoc> findAll() {
        return wrap(dataAccess().findAll());
    }
}
