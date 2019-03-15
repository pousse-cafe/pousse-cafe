package poussecafe.doc.model.processstepdoc;

import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.domain.EntityDataAccess;

public interface ProcessStepDataAccess<D extends ProcessStepDoc.Attributes> extends EntityDataAccess<ProcessStepDocKey, D> {

    D getByStepMethodSignature(BoundedContextDocKey boundedContextDocKey,
            StepMethodSignature stepMethodSignature);
}
