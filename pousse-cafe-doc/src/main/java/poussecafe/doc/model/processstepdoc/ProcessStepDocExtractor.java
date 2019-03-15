package poussecafe.doc.model.processstepdoc;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import poussecafe.doc.AnnotationsResolver;
import poussecafe.doc.Logger;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.ComponentDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.doc.model.domainprocessdoc.ComponentMethodName;
import poussecafe.domain.Service;

import static java.util.stream.Collectors.toList;

public class ProcessStepDocExtractor implements Service {

    public List<ProcessStepDoc> extractProcessStepDocs(BoundedContextDocKey boundedContextDocKey, ClassDoc classDoc) {
        List<ProcessStepDoc> stepDocs = new ArrayList<>();
        for(MethodDoc methodDoc : classDoc.methods()) {
            Optional<String> consumedDomainEvent = consumedMessage(methodDoc);
            List<String> producedEvents = extractProducedEvents(methodDoc);
            if(AnnotationsResolver.isStep(methodDoc) ||
                    (methodDoc.isPublic() && (consumedDomainEvent.isPresent() || !producedEvents.isEmpty()))) {
                List<String> customStepSignatures = AnnotationsResolver.step(methodDoc);
                if(!customStepSignatures.isEmpty()) {
                    List<StepMethodSignature> methodSignatures = customStepSignatures.stream().map(StepMethodSignature::parse).collect(toList());
                    for(StepMethodSignature signature : methodSignatures) {
                        Logger.info("Extracting custom step " + signature);
                        ProcessStepDocKey messageListenerDocKey = new ProcessStepDocKey(signature.toString());
                        BoundedContextComponentDoc boundedContextComponentDoc = new BoundedContextComponentDoc.Builder()
                                .boundedContextDocKey(boundedContextDocKey)
                                .componentDoc(new ComponentDoc.Builder()
                                        .name(signature.toString())
                                        .description(methodDoc.commentText())
                                        .build())
                                .build();
                        stepDocs.add(messageListenerDocFactory.createMessageListenerDoc(messageListenerDocKey,
                                boundedContextComponentDoc,
                                signature,
                                producedEvents));
                    }
                } else {
                    Logger.info("Extracting declared step from method " + methodDoc.qualifiedName());
                    StepMethodSignature stepMethodSignature = new StepMethodSignature.Builder()
                            .componentMethodName(new ComponentMethodName.Builder()
                                    .componentName(classDoc.name())
                                    .methodName(methodDoc.name())
                                    .build())
                            .consumedEventName(consumedDomainEvent)
                            .build();
                    stepDocs.add(messageListenerDocFactory.createMessageListenerDoc(boundedContextDocKey, stepMethodSignature, methodDoc));
                }
            }
        }
        return stepDocs;
    }

    private ProcessStepDocFactory messageListenerDocFactory;

    private Optional<String> consumedMessage(MethodDoc methodDoc) {
        return new ConsumedMessageExtractor(methodDoc).consumedDomainEvent();
    }

    private List<String> extractProducedEvents(MethodDoc methodDoc) {
        return AnnotationsResolver.event(methodDoc);
    }
}
