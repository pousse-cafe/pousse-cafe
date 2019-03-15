package poussecafe.doc.model.processstepdoc;

import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.doc.model.processstepdoc.ProcessStepDoc.Attributes;
import poussecafe.domain.Repository;

public class ProcessStepDocRepository extends Repository<ProcessStepDoc, ProcessStepDocKey, ProcessStepDoc.Attributes> {

    public ProcessStepDoc getByStepMethodSignature(BoundedContextDocKey boundedContextDocKey,
            StepMethodSignature stepMethodSignature) {
        return wrap(dataAcces().getByStepMethodSignature(boundedContextDocKey, stepMethodSignature));
    }

    private ProcessStepDataAccess<ProcessStepDoc.Attributes> dataAcces() {
        return (ProcessStepDataAccess<Attributes>) super.dataAccess();
    }
}
