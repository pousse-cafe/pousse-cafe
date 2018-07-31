package poussecafe.doc.model.step;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import poussecafe.doc.AnnotationsResolver;
import poussecafe.doc.Logger;
import poussecafe.doc.model.ComponentDoc;
import poussecafe.doc.model.domainprocessdoc.ComponentMethodName;
import poussecafe.domain.Service;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class StepDocExtractor implements Service {

    public List<StepDoc> extractStepDocs(String componentName, ClassDoc classDoc) {
        List<StepDoc> stepDocs = new ArrayList<>();
        for(MethodDoc methodDoc : classDoc.methods()) {
            Optional<String> consumedDomainEvent = consumedDomainEvent(methodDoc);
            List<String> producedEvents = extractProducedEvents(methodDoc);
            if(AnnotationsResolver.isStep(methodDoc) ||
                    (methodDoc.isPublic() && (consumedDomainEvent.isPresent() || !producedEvents.isEmpty()))) {
                List<String> customStepSignatures = AnnotationsResolver.step(methodDoc);
                List<StepMethodSignature> methodSignatures;
                if(!customStepSignatures.isEmpty()) {
                    methodSignatures = customStepSignatures.stream().map(StepMethodSignature::parse).collect(toList());
                } else {
                    methodSignatures = asList(buildSignature(componentName, methodDoc, consumedDomainEvent));
                }
                for(StepMethodSignature signature : methodSignatures) {
                    Logger.info("Extracting step " + signature);
                    stepDocs.add(new StepDoc.Builder()
                            .methodSignature(signature)
                            .producedEvents(producedEvents)
                            .componentDoc(new ComponentDoc.Builder()
                                    .name(signature.toString())
                                    .description(methodDoc.commentText())
                                    .build())
                            .build());
                }
            }
        }
        return stepDocs;
    }

    private Optional<String> consumedDomainEvent(MethodDoc methodDoc) {
        return new ConsumedDomainEventExtractor(methodDoc).consumedDomainEvent();
    }

    private List<String> extractProducedEvents(MethodDoc methodDoc) {
        return AnnotationsResolver.event(methodDoc);
    }

    private StepMethodSignature buildSignature(String componentName, MethodDoc methodDoc, Optional<String> consumedEventName) {
        return new StepMethodSignature.Builder()
                .componentMethodName(new ComponentMethodName.Builder()
                        .aggregateName(componentName)
                        .methodName(methodDoc.name())
                        .build())
                .consumedEventName(consumedEventName)
                .build();
    }
}
