package poussecafe.doc.model.servicedoc;

import java.util.List;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.domain.AggregateRepository;

public class ServiceDocRepository extends AggregateRepository<ServiceDoc, ServiceDocId, ServiceDoc.Attributes> {

    public List<ServiceDoc> findByModuleId(ModuleDocId id) {
        return wrap(dataAccess().findByModuleDocId(id));
    }

    @Override
    public ServiceDocDataAccess<ServiceDoc.Attributes> dataAccess() {
        return (ServiceDocDataAccess<ServiceDoc.Attributes>) super.dataAccess();
    }

    public List<ServiceDoc> findAll() {
        return wrap(dataAccess().findAll());
    }
}
