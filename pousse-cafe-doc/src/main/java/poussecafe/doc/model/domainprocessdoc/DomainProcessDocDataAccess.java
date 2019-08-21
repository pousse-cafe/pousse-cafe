package poussecafe.doc.model.domainprocessdoc;

import java.util.List;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.domain.EntityDataAccess;

public interface DomainProcessDocDataAccess<D extends DomainProcessDoc.Attributes> extends EntityDataAccess<DomainProcessDocId, D> {

    List<D> findByModuleId(ModuleDocId id);
}
