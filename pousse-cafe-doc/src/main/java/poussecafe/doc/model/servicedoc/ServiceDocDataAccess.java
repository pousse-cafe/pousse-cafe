package poussecafe.doc.model.servicedoc;

import java.util.List;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.domain.EntityDataAccess;

public interface ServiceDocDataAccess<D extends ServiceDoc.Attributes> extends EntityDataAccess<ServiceDocId, D> {

    List<D> findByModuleDocId(ModuleDocId id);

}
