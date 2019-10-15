package poussecafe.doc.model.processstepdoc;

import poussecafe.doc.model.ModuleComponentDoc;
import poussecafe.domain.Factory;

public class ProcessStepDocFactory extends Factory<ProcessStepDocId, ProcessStepDoc, ProcessStepDoc.Attributes> {

    public ProcessStepDoc createMessageListenerDoc(
            ProcessStepDocId id,
            ModuleComponentDoc moduleComponentDoc) {
        ProcessStepDoc doc = newAggregateWithId(id);
        doc.attributes().moduleComponentDoc().value(moduleComponentDoc);
        return doc;
    }
}
