package poussecafe.doc.model.processstepdoc;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import poussecafe.doc.AnnotationsResolver;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.Logger;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.ComponentDoc;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.doc.model.domainprocessdoc.ComponentMethodName;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocFactory;
import poussecafe.domain.DomainEvent;
import poussecafe.domain.Service;
import poussecafe.exception.PousseCafeException;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class ProcessStepDocExtractor implements Service {

    public List<ProcessStepDoc> extractProcessStepDocs(BoundedContextDocId boundedContextDocId, ClassDoc classDoc) {
        List<ProcessStepDoc> stepDocs = new ArrayList<>();
        for(MethodDoc methodDoc : classDoc.methods()) {
            if(isProcessStep(methodDoc)) {
                List<String> customStepSignatures = AnnotationsResolver.step(methodDoc);
                if(!customStepSignatures.isEmpty()) {
                    stepDocs.addAll(extractCustomSteps(boundedContextDocId, methodDoc));
                } else {
                    stepDocs.add(extractDeclaredStep(boundedContextDocId, methodDoc));
                }
            }
        }
        return stepDocs;
    }

    private boolean isProcessStep(MethodDoc methodDoc) {
        if(DomainProcessDocFactory.isDomainProcessDoc(methodDoc.containingClass())) {
            return AnnotationsResolver.isStep(methodDoc);
        } else {
            Optional<String> consumedMessage = consumedMessage(methodDoc);
            List<String> producedEvents = extractProducedEvents(methodDoc);
            return AnnotationsResolver.isStep(methodDoc) ||
                    (methodDoc.isPublic() && (consumedMessage.isPresent() || !producedEvents.isEmpty()));
        }
    }

    private Optional<String> consumedMessage(MethodDoc methodDoc) {
        return new ConsumedMessageExtractor(methodDoc).consumedMessage();
    }

    private List<String> extractProducedEvents(MethodDoc methodDoc) {
        return AnnotationsResolver.event(methodDoc);
    }

    private List<ProcessStepDoc> extractCustomSteps(BoundedContextDocId boundedContextDocId,
            MethodDoc methodDoc) {
        List<ProcessStepDoc> stepDocs = new ArrayList<>();
        List<StepMethodSignature> methodSignatures = customStepsSignatures(methodDoc);
        for(StepMethodSignature signature : methodSignatures) {
            Logger.info("Extracting custom step " + signature);
            ProcessStepDocId messageListenerDocId = new ProcessStepDocId(signature.toString());
            BoundedContextComponentDoc boundedContextComponentDoc = new BoundedContextComponentDoc.Builder()
                    .boundedContextDocId(boundedContextDocId)
                    .componentDoc(new ComponentDoc.Builder()
                            .name(signature.toString())
                            .description(methodDoc.commentText())
                            .build())
                    .build();
            ProcessStepDoc processStepDoc = messageListenerDocFactory.createMessageListenerDoc(messageListenerDocId,
                    boundedContextComponentDoc);
            processStepDoc.attributes().processName().value(processName(methodDoc));
            processStepDoc.attributes().stepMethodSignature().nonOptionalValue(signature);
            processStepDoc.attributes().producedEvents().value(new HashSet<>(AnnotationsResolver.event(methodDoc)));
            processStepDoc.attributes().fromExternals().value(new HashSet<>(AnnotationsResolver.fromExternal(methodDoc)));
            processStepDoc.attributes().toExternals().value(new HashSet<>(AnnotationsResolver.toExternal(methodDoc)));
            stepDocs.add(processStepDoc);
        }
        return stepDocs;
    }

    private List<StepMethodSignature> customStepsSignatures(MethodDoc methodDoc) {
        List<String> customStepSignatures = AnnotationsResolver.step(methodDoc);
        if(DomainProcessDocFactory.isDomainProcessDoc(methodDoc.containingClass())) {
            if(customStepSignatures.size() != 1) {
                throw new PousseCafeException("Domain processes listeners must be tagged with a single step");
            }
            Optional<String> consumedEvent = consumedEvent(methodDoc);
            ComponentMethodName componentMethodName = ComponentMethodName.parse(customStepSignatures.get(0));
            return asList(new StepMethodSignature.Builder()
                    .componentMethodName(componentMethodName)
                    .consumedMessageName(consumedEvent)
                    .build());
        } else {
            return customStepSignatures.stream().map(StepMethodSignature::parse).collect(toList());
        }
    }

    private Optional<String> consumedEvent(MethodDoc methodDoc) {
        if(methodDoc.parameters().length > 0 &&
                methodDoc.parameters()[0].type().asClassDoc() != null &&
                ClassDocPredicates.documentsWithSuperinterface(methodDoc.parameters()[0].type().asClassDoc(), DomainEvent.class)) {
            return Optional.of(methodDoc.parameters()[0].type().asClassDoc().name());
        } else {
            return Optional.empty();
        }
    }

    private ProcessStepDocFactory messageListenerDocFactory;

    private Optional<String> processName(MethodDoc methodDoc) {
        ClassDoc containingClass = methodDoc.containingClass();
        List<String> processNames = AnnotationsResolver.process(methodDoc);
        if(DomainProcessDocFactory.isDomainProcessDoc(containingClass)) {
            return Optional.of(DomainProcessDocFactory.name(containingClass));
        } else if(!processNames.isEmpty()) {
            return Optional.of(processNames.get(0));
        } else {
            return Optional.empty();
        }
    }

    private ProcessStepDoc extractDeclaredStep(BoundedContextDocId boundedContextDocId,
            MethodDoc methodDoc) {
        Logger.info("Extracting declared step from method " + methodDoc.qualifiedName());
        Optional<String> consumedMessage = consumedMessage(methodDoc);
        StepMethodSignature stepMethodSignature = new StepMethodSignature.Builder()
                .componentMethodName(new ComponentMethodName.Builder()
                        .componentName(methodDoc.containingClass().name())
                        .methodName(methodDoc.name())
                        .build())
                .consumedMessageName(consumedMessage)
                .build();
        ProcessStepDocId id = new ProcessStepDocId(stepMethodSignature);
        BoundedContextComponentDoc boundedContextComponentDoc = new BoundedContextComponentDoc.Builder()
                .boundedContextDocId(boundedContextDocId)
                .componentDoc(componentDocFactory.buildDoc(id.getValue(), methodDoc))
                .build();
        ProcessStepDoc processStepDoc = messageListenerDocFactory.createMessageListenerDoc(id,
                boundedContextComponentDoc);
        processStepDoc.attributes().processName().value(processName(methodDoc));
        processStepDoc.attributes().stepMethodSignature().nonOptionalValue(stepMethodSignature);
        processStepDoc.attributes().producedEvents().value(new HashSet<>(AnnotationsResolver.event(methodDoc)));
        processStepDoc.attributes().fromExternals().value(new HashSet<>(AnnotationsResolver.fromExternal(methodDoc)));
        processStepDoc.attributes().toExternals().value(new HashSet<>(AnnotationsResolver.toExternal(methodDoc)));
        return processStepDoc;
    }

    private ComponentDocFactory componentDocFactory;
}
