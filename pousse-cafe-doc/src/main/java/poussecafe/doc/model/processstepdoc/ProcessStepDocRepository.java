package poussecafe.doc.model.processstepdoc;

import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.doc.model.processstepdoc.ProcessStepDoc.Attributes;
import poussecafe.domain.Repository;

public class ProcessStepDocRepository extends Repository<ProcessStepDoc, ProcessStepDocKey, ProcessStepDoc.Attributes> {

    public List<ProcessStepDoc> findByDomainProcess(BoundedContextDocKey boundedContextDocKey,
            String processName) {
        return wrap(dataAccess().findByDomainProcess(boundedContextDocKey, processName));
    }

    @Override
    public ProcessStepDataAccess<ProcessStepDoc.Attributes> dataAccess() {
        return (ProcessStepDataAccess<Attributes>) super.dataAccess();
    }
}
