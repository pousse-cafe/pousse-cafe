package poussecafe.doc.model.processstepdoc;

import com.sun.javadoc.MethodDoc;
import java.util.List;
import poussecafe.doc.AnnotationsResolver;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.domain.Factory;

public class ProcessStepDocFactory extends Factory<ProcessStepDocKey, ProcessStepDoc, ProcessStepDoc.Attributes> {

    public ProcessStepDoc createMessageListenerDoc(BoundedContextDocKey boundedContextDocKey,
            StepMethodSignature stepMethodSignature,
            MethodDoc messageListenerMethod) {
        ProcessStepDocKey key = new ProcessStepDocKey(stepMethodSignature);
        return createMessageListenerDoc(
                key,
                new BoundedContextComponentDoc.Builder()
                    .boundedContextDocKey(boundedContextDocKey)
                    .componentDoc(componentDocFactory.buildDoc(key.getValue(), messageListenerMethod))
                    .build(),
                stepMethodSignature,
                extractProducedEvents(messageListenerMethod));
    }

    private ComponentDocFactory componentDocFactory;

    private List<String> extractProducedEvents(MethodDoc methodDoc) {
        return AnnotationsResolver.event(methodDoc);
    }

    public ProcessStepDoc createMessageListenerDoc(
            ProcessStepDocKey key,
            BoundedContextComponentDoc boundedContextComponentDoc,
            StepMethodSignature stepMethodSignature,
            List<String> producedEvents) {
        ProcessStepDoc doc = newAggregateWithKey(key);

        doc.attributes().boundedContextComponentDoc().value(boundedContextComponentDoc);

        doc.attributes().stepMethodSignature().value(stepMethodSignature);
        doc.attributes().producedEvents().value(producedEvents);

        return doc;
    }
}
