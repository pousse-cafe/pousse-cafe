package poussecafe.doc.model.processstepdoc;

import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.domain.Factory;

public class ProcessStepDocFactory extends Factory<ProcessStepDocId, ProcessStepDoc, ProcessStepDoc.Attributes> {

    public ProcessStepDoc createMessageListenerDoc(
            ProcessStepDocId id,
            BoundedContextComponentDoc boundedContextComponentDoc) {
        ProcessStepDoc doc = newAggregateWithId(id);
        doc.attributes().boundedContextComponentDoc().value(boundedContextComponentDoc);
        return doc;
    }
}
