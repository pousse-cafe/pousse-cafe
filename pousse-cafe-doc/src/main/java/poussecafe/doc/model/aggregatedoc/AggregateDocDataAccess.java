package poussecafe.doc.model.aggregatedoc;

import java.util.List;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.domain.EntityDataAccess;

public interface AggregateDocDataAccess<D extends AggregateDoc.Attributes> extends EntityDataAccess<AggregateDocId, D> {

    List<D> findByModuleId(ModuleDocId id);

    List<D> findByIdClassName(String qualifiedName);

    D findByModuleIdAndName(ModuleDocId moduleDocId,
            String aggregateName);

}
