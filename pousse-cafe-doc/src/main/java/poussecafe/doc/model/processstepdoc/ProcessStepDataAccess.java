package poussecafe.doc.model.processstepdoc;

import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.domain.EntityDataAccess;

public interface ProcessStepDataAccess<D extends ProcessStepDoc.Attributes> extends EntityDataAccess<ProcessStepDocId, D> {

    List<D> findByDomainProcess(BoundedContextDocId boundedContextDocId,
            String processName);
}
