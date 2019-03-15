package poussecafe.doc.model.processstepdoc;

import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.domain.EntityDataAccess;

public interface ProcessStepDataAccess<D extends ProcessStepDoc.Attributes> extends EntityDataAccess<ProcessStepDocKey, D> {

    List<D> findByDomainProcess(BoundedContextDocKey boundedContextDocKey,
            String processName);
}
