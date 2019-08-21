package poussecafe.doc.model.processstepdoc;

import java.util.List;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.domain.EntityDataAccess;

public interface ProcessStepDataAccess<D extends ProcessStepDoc.Attributes> extends EntityDataAccess<ProcessStepDocId, D> {

    List<D> findByDomainProcess(ModuleDocId moduleDocId,
            String processName);

    List<D> findConsuming(ModuleDocId moduleDocId,
            String eventName);

    List<D> findProducing(ModuleDocId moduleDocId,
            String eventName);
}
