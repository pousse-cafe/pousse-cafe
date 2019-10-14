package poussecafe.doc.model.processstepdoc;

import java.util.Optional;
import poussecafe.doc.model.ModuleComponentDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocId;
import poussecafe.domain.Factory;

public class ProcessStepDocFactory extends Factory<ProcessStepDocId, ProcessStepDoc, ProcessStepDoc.Attributes> {

    public ProcessStepDoc createMessageListenerDoc(
            ProcessStepDocId id,
            ModuleComponentDoc moduleComponentDoc,
            Optional<AggregateDocId> aggregate) {
        ProcessStepDoc doc = newAggregateWithId(id);
        doc.attributes().moduleComponentDoc().value(moduleComponentDoc);
        doc.attributes().aggregate().value(aggregate);
        return doc;
    }
}
