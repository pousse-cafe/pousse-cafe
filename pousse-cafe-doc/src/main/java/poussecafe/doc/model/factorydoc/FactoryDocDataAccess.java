package poussecafe.doc.model.factorydoc;

import java.util.List;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.domain.EntityDataAccess;

public interface FactoryDocDataAccess<D extends FactoryDoc.Attributes> extends EntityDataAccess<FactoryDocId, D> {

    List<D> findByModuleId(ModuleDocId id);

    D findByModuleIdAndName(ModuleDocId moduleDocId,
            String factoryName);
}
