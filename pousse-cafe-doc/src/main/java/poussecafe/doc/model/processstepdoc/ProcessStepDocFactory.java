package poussecafe.doc.model.processstepdoc;

import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.domain.Factory;

public class ProcessStepDocFactory extends Factory<ProcessStepDocKey, ProcessStepDoc, ProcessStepDoc.Attributes> {

    public ProcessStepDoc createMessageListenerDoc(
            ProcessStepDocKey key,
            BoundedContextComponentDoc boundedContextComponentDoc) {
        ProcessStepDoc doc = newAggregateWithKey(key);
        doc.attributes().boundedContextComponentDoc().value(boundedContextComponentDoc);
        return doc;
    }
}
