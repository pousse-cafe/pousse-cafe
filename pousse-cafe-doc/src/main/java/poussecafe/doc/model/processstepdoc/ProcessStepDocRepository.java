package poussecafe.doc.model.processstepdoc;

import java.util.List;
import poussecafe.doc.model.aggregatedoc.AggregateDocId;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.doc.model.processstepdoc.ProcessStepDoc.Attributes;
import poussecafe.domain.Repository;

public class ProcessStepDocRepository extends Repository<ProcessStepDoc, ProcessStepDocId, ProcessStepDoc.Attributes> {

    public List<ProcessStepDoc> findByDomainProcess(ModuleDocId moduleDocId,
            String processName) {
        return wrap(dataAccess().findByDomainProcess(moduleDocId, processName));
    }

    @Override
    public ProcessStepDataAccess<ProcessStepDoc.Attributes> dataAccess() {
        return (ProcessStepDataAccess<Attributes>) super.dataAccess();
    }

    public List<ProcessStepDoc> findConsuming(ModuleDocId moduleDocId,
            String eventName) {
        return wrap(dataAccess().findConsuming(moduleDocId, eventName));
    }

    public List<ProcessStepDoc> findProducing(ModuleDocId moduleDocId,
            String eventName) {
        return wrap(dataAccess().findProducing(moduleDocId, eventName));
    }

    public List<ProcessStepDoc> findByAggregateDocId(AggregateDocId aggregateDocId) {
        return wrap(dataAccess().findByAggregateDocId(aggregateDocId));
    }
}
