package poussecafe.doc.model.processstepdoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import jdk.javadoc.doclet.DocletEnvironment;
import poussecafe.discovery.MessageListener;
import poussecafe.discovery.ProducesEvent;
import poussecafe.discovery.ProducesEvents;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.Logger;
import poussecafe.doc.annotations.AnnotationUtils;
import poussecafe.doc.model.AnnotationsResolver;
import poussecafe.doc.model.ComponentDoc;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.doc.model.DocletAccess;
import poussecafe.doc.model.ModuleComponentDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocFactory;
import poussecafe.doc.model.aggregatedoc.AggregateDocId;
import poussecafe.doc.model.domainprocessdoc.ComponentMethodName;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocFactory;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.domain.DomainEvent;
import poussecafe.domain.Service;
import poussecafe.exception.PousseCafeException;
import poussecafe.source.generation.NamingConventions;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;
import static poussecafe.collection.Collections.asSet;

public class ProcessStepDocExtractor implements Service {

    public List<ProcessStepDoc> extractProcessStepDocs(ModuleDocId moduleDocId, TypeElement classDoc) {
        List<ProcessStepDoc> stepDocs = new ArrayList<>();
        for(ExecutableElement methodDoc : docletAccess.methods(classDoc)) {
            if(isProcessStep(methodDoc)) {
                List<String> customStepSignatures = annotationsResolver.step(methodDoc);
                if(!customStepSignatures.isEmpty()) {
                    stepDocs.addAll(extractCustomSteps(moduleDocId, methodDoc));
                } else {
                    stepDocs.add(extractDeclaredStep(moduleDocId, methodDoc));
                }
            }
        }
        return stepDocs;
    }

    private DocletAccess docletAccess;

    private boolean isProcessStep(ExecutableElement methodDoc) {
        if(domainProcessDocFactory.isDomainProcessDoc((TypeElement) methodDoc.getEnclosingElement())) {
            return annotationsResolver.isStep(methodDoc);
        } else {
            Optional<String> consumedMessage = consumedMessageExtractor.consumedMessage(methodDoc);
            Set<NameRequired> producedEvents = extractProducedEvents(methodDoc);
            return annotationsResolver.isStep(methodDoc) ||
                    (docletAccess.isPublic(methodDoc) && (consumedMessage.isPresent() || !producedEvents.isEmpty()));
        }
    }

    private DomainProcessDocFactory domainProcessDocFactory;

    private AnnotationsResolver annotationsResolver;

    private ConsumedMessageExtractor consumedMessageExtractor;

    private Set<NameRequired> extractProducedEvents(ExecutableElement methodDoc) {
        Set<NameRequired> producedEvents = new HashSet<>();
        List<String> javadocTagEvents = annotationsResolver.event(methodDoc);
        if(!javadocTagEvents.isEmpty()) {
            Logger.warn("@event tag is deprecated, use @ProducesEvent annotation instead");
            producedEvents.addAll(javadocTagEvents.stream().map(NameRequired::required).collect(toList()));
        }
        List<? extends AnnotationMirror> producesEventAnnotations = producesEventAnnotations(methodDoc);
        for(AnnotationMirror mirror : producesEventAnnotations) {
            NameRequired nameRequired = nameRequired(mirror);
            producedEvents.add(nameRequired);
        }
        return producedEvents;
    }

    private List<AnnotationMirror> producesEventAnnotations(ExecutableElement methodDoc) {
        return AnnotationUtils.annotations(methodDoc, ProducesEvent.class, ProducesEvents.class);
    }

    private NameRequired nameRequired(AnnotationMirror producesEventMirror) {
        Optional<AnnotationValue> producesEventValue = AnnotationUtils.value(producesEventMirror, "value");
        Element valueElement = producesEventValue
                .map(AnnotationValue::getValue)
                .map(value -> docletAccess.getTypesUtils().asElement((TypeMirror) value))
                .orElseThrow();

        boolean required = required(producesEventMirror);
        NameRequired nameRequired;
        if(required) {
            nameRequired = NameRequired.required(valueElement.getSimpleName().toString());
        } else {
            nameRequired = NameRequired.optional(valueElement.getSimpleName().toString());
        }
        return nameRequired;
    }

    private boolean required(AnnotationMirror producesEventMirror) {
        Optional<AnnotationValue> producesEventRequired = AnnotationUtils.value(producesEventMirror, "required");
        boolean required;
        if(producesEventRequired.isPresent()) {
            required = (Boolean) producesEventRequired.get().getValue();
        } else {
            required = true;
        }
        return required;
    }

    private List<ProcessStepDoc> extractCustomSteps(ModuleDocId moduleDocId,
            ExecutableElement methodDoc) {
        List<ProcessStepDoc> stepDocs = new ArrayList<>();
        Set<String> processNames = processNames(methodDoc);
        Set<NameRequired> producedEvents = extractProducedEvents(methodDoc);
        Set<String> fromExternals = extractFromExternals(methodDoc);
        Set<String> toExternals = extractToExternals(methodDoc);
        Map<NameRequired, List<String>> toExternalsByEvent = extractToExternalsByEvent(methodDoc);

        List<StepMethodSignature> methodSignatures = customStepsSignatures(methodDoc);
        for(StepMethodSignature signature : methodSignatures) {
            Logger.info("Extracting custom step " + signature);
            ProcessStepDocId messageListenerDocId = new ProcessStepDocId(signature.toString());
            ModuleComponentDoc moduleComponentDoc = new ModuleComponentDoc.Builder()
                    .moduleDocId(moduleDocId)
                    .componentDoc(new ComponentDoc.Builder()
                            .name(signature.toString())
                            .description(annotationsResolver.renderCommentBody(methodDoc))
                            .build())
                    .build();

            ProcessStepDoc processStepDoc = messageListenerDocFactory.createMessageListenerDoc(messageListenerDocId,
                    moduleComponentDoc);
            processStepDoc.attributes().processNames().value(processNames);
            processStepDoc.attributes().stepMethodSignature().value(Optional.of(signature));
            processStepDoc.attributes().producedEvents().value(producedEvents);
            processStepDoc.attributes().fromExternals().value(fromExternals);
            processStepDoc.attributes().toExternals().value(toExternals);
            processStepDoc.attributes().toExternalsByEvent().value(toExternalsByEvent);
            stepDocs.add(processStepDoc);
        }
        return stepDocs;
    }

    private Set<String> extractToExternals(ExecutableElement methodDoc) {
        Set<String> toExternals = new HashSet<>();
        List<String> javadocTagToExternals = annotationsResolver.toExternal(methodDoc);
        if(!javadocTagToExternals.isEmpty()) {
            Logger.warn("@to_external tag is deprecated, use @ProducesEvent annotation and set consumedByExternal element instead");
            toExternals.addAll(javadocTagToExternals);
        }
        return toExternals;
    }

    private static final String CONSUMED_BY_EXTERNAL_ELEMENT_NAME = "consumedByExternal";

    @SuppressWarnings("unchecked")
    private Set<String> extractFromExternals(ExecutableElement methodDoc) {
        Set<String> fromExternals = new HashSet<>();
        List<String> javadocTagToExternals = annotationsResolver.fromExternal(methodDoc);
        if(!javadocTagToExternals.isEmpty()) {
            Logger.warn("@from_external tag is deprecated, use @MessageListener annotation and set consumesFromExternal element instead");
            fromExternals.addAll(javadocTagToExternals);
        }

        Optional<AnnotationMirror> annotationMirror = AnnotationUtils.annotation(methodDoc, MessageListener.class);
        if(annotationMirror.isPresent()) {
           Optional<AnnotationValue> optionalAnnotationValue = AnnotationUtils.value(annotationMirror.get(), "consumesFromExternal");
           if(optionalAnnotationValue.isPresent()) {
               List<AnnotationValue> annotationValue = (List<AnnotationValue>) optionalAnnotationValue.get().getValue();
               fromExternals.addAll(annotationValue.stream()
                       .map(external -> (String) external.getValue())
                       .collect(toList()));
           }
        }

        return fromExternals;
    }

    @SuppressWarnings("unchecked")
    private Map<NameRequired, List<String>> extractToExternalsByEvent(ExecutableElement methodDoc) {
        Map<NameRequired, List<String>> toExternals = new HashMap<>();
        List<? extends AnnotationMirror> producesEventAnnotations = producesEventAnnotations(methodDoc);
        for(AnnotationMirror mirror : producesEventAnnotations) {
            NameRequired nameRequired = nameRequired(mirror);
            Optional<AnnotationValue> consumedByExternal = AnnotationUtils.value(mirror, CONSUMED_BY_EXTERNAL_ELEMENT_NAME);
            if(consumedByExternal.isPresent()) {
                List<AnnotationValue> externals = (List<AnnotationValue>) consumedByExternal.get().getValue();
                toExternals.put(nameRequired, externals.stream()
                        .map(annotationValue -> (String) annotationValue.getValue())
                        .collect(toList()));
            }
        }
        return toExternals;
    }

    private List<StepMethodSignature> customStepsSignatures(ExecutableElement methodDoc) {
        List<String> customStepSignatures = annotationsResolver.step(methodDoc);
        if(domainProcessDocFactory.isDomainProcessDoc((TypeElement) methodDoc.getEnclosingElement())) {
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

    private Optional<String> consumedEvent(ExecutableElement methodDoc) {
        List<? extends VariableElement> parameters = methodDoc.getParameters();
        if(parameters.isEmpty()) {
            return Optional.empty();
        }

        TypeMirror firstParameterType = parameters.get(0).asType();
        Element firstParameterElement = docletEnvironment.getTypeUtils().asElement(firstParameterType);
        if(firstParameterElement instanceof TypeElement) {
            TypeElement firstParameterTypeElement = (TypeElement) firstParameterElement;
            if(classDocPredicates.documentsWithSuperinterface(firstParameterTypeElement, DomainEvent.class)) {
                return Optional.of(firstParameterTypeElement.getQualifiedName().toString());
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    private DocletEnvironment docletEnvironment;

    private ClassDocPredicates classDocPredicates;

    private ProcessStepDocFactory messageListenerDocFactory;

    private Set<String> processNames(ExecutableElement methodDoc) {
        Set<String> processNames = new HashSet<>(annotationsResolver.process(methodDoc));
        processNames.addAll(namesFromMessageListenerAnnotation(methodDoc));
        TypeElement containingClass = (TypeElement) methodDoc.getEnclosingElement();
        if(domainProcessDocFactory.isDomainProcessDoc(containingClass)) {
            return asSet(domainProcessDocFactory.name(containingClass));
        } else if(!processNames.isEmpty()) {
            return Collections.unmodifiableSet(processNames);
        } else {
            return emptySet();
        }
    }

    private List<String> namesFromMessageListenerAnnotation(ExecutableElement methodDoc) {
        Optional<AnnotationMirror> annotationMirror = AnnotationUtils.annotation(methodDoc, MessageListener.class);
        if(annotationMirror.isPresent()) {
            Optional<AnnotationValue> value = AnnotationUtils.value(annotationMirror.get(), "processes");
            if(value.isPresent()) {
                @SuppressWarnings("unchecked")
                List<AnnotationValue> processes = (List<AnnotationValue>) value.get().getValue();
                return processes.stream()
                        .map(processValue -> docletAccess.getTypesUtils().asElement((TypeMirror) processValue.getValue()))
                        .map(process -> process.getSimpleName().toString())
                        .collect(toList());
            } else {
                return emptyList();
            }
        } else {
            return emptyList();
        }
    }

    private ProcessStepDoc extractDeclaredStep(ModuleDocId moduleDocId,
            ExecutableElement methodDoc) {
        Logger.info("Extracting declared step from method " + methodDoc.getSimpleName().toString());

        Set<String> processNames = processNames(methodDoc);
        Set<NameRequired> producedEvents = extractProducedEvents(methodDoc);
        Set<String> fromExternals = extractFromExternals(methodDoc);
        Set<String> toExternals = extractToExternals(methodDoc);
        Map<NameRequired, List<String>> toExternalsByEvent = extractToExternalsByEvent(methodDoc);

        Optional<String> consumedMessage = consumedMessageExtractor.consumedMessage(methodDoc);
        TypeElement enclosingType = (TypeElement) methodDoc.getEnclosingElement();
        var componentName = listenerContainerName(enclosingType);
        StepMethodSignature stepMethodSignature = new StepMethodSignature.Builder()
                .componentMethodName(new ComponentMethodName.Builder()
                        .componentName(componentName)
                        .methodName(methodDoc.getSimpleName().toString())
                        .build())
                .consumedMessageName(consumedMessage)
                .build();

        if(aggregateDocFactory.isFactoryDoc(enclosingType)) {
            TypeElement aggregateTypeElement = aggregateDocFactory.aggregateTypeElementOfFactory(enclosingType);
            List<ExecutableElement> methods = ElementFilter.methodsIn(aggregateTypeElement.getEnclosedElements());
            Optional<ExecutableElement> onAddMethod = methods.stream()
                    .filter(element -> element.getSimpleName().toString().equals("onAdd"))
                    .filter(element -> element.getParameters().isEmpty())
                    .findFirst();
            if(onAddMethod.isPresent()) {
                ExecutableElement presentOnAddMethod = onAddMethod.get();
                producedEvents.addAll(extractProducedEvents(presentOnAddMethod));
                toExternals.addAll(extractToExternals(presentOnAddMethod));
                toExternalsByEvent.putAll(extractToExternalsByEvent(presentOnAddMethod));
            }
        }

        AggregateDocId aggregate = declaringAggregate(methodDoc);

        ProcessStepDocId id = new ProcessStepDocId(stepMethodSignature);
        ModuleComponentDoc moduleComponentDoc = new ModuleComponentDoc.Builder()
                .moduleDocId(moduleDocId)
                .componentDoc(componentDocFactory.buildDoc(id.stringValue(), methodDoc))
                .build();

        ProcessStepDoc processStepDoc = messageListenerDocFactory.createMessageListenerDoc(id,
                moduleComponentDoc);
        processStepDoc.attributes().processNames().value(processNames);
        processStepDoc.attributes().stepMethodSignature().value(Optional.of(stepMethodSignature));
        processStepDoc.attributes().producedEvents().value(producedEvents);
        processStepDoc.attributes().fromExternals().value(fromExternals);
        processStepDoc.attributes().toExternals().value(toExternals);
        processStepDoc.attributes().toExternalsByEvent().value(toExternalsByEvent);
        processStepDoc.attributes().aggregate().value(Optional.of(aggregate));

        return processStepDoc;
    }

    private String listenerContainerName(TypeElement enclosingType) {
        String componentName;
        if(aggregateDocFactory.isStandaloneRoot(enclosingType)) {
            componentName = NamingConventions.aggregateNameFromSimpleRootName(enclosingType.getSimpleName().toString());
        } else if(aggregateDocFactory.isStandaloneFactory(enclosingType)) {
            componentName = NamingConventions.aggregateNameFromSimpleFactoryName(enclosingType.getSimpleName().toString());
        } else if(aggregateDocFactory.isStandaloneRepository(enclosingType)) {
            componentName = NamingConventions.aggregateNameFromSimpleRepositoryName(enclosingType.getSimpleName().toString());
        } else {
            var potentialContainer = enclosingType.getEnclosingElement();
            if(potentialContainer instanceof TypeElement
                    && aggregateDocFactory.isContainer((TypeElement) potentialContainer)) {
                componentName = potentialContainer.getSimpleName().toString();
            } else {
                componentName = enclosingType.getSimpleName().toString();
            }
        }
        return componentName;
    }

    private AggregateDocId declaringAggregate(ExecutableElement methodDoc) {
        TypeElement enclosingType = (TypeElement) methodDoc.getEnclosingElement();
        TypeElement aggregateType;
        if(aggregateDocFactory.isFactoryDoc(enclosingType)) {
            aggregateType = aggregateDocFactory.aggregateTypeElementOfFactory(enclosingType);
        } else if(aggregateDocFactory.isRepositoryDoc(enclosingType)) {
            aggregateType = aggregateDocFactory.aggregateTypeElementOfRepository(enclosingType);
        } else if(aggregateDocFactory.extendsAggregateRoot(enclosingType)) {
            aggregateType = aggregateDocFactory.aggregateTypeElementOfRoot(enclosingType);
        } else {
            aggregateType = enclosingType;
        }
        return AggregateDocId.ofClassName(aggregateType.getQualifiedName().toString());
    }

    private ComponentDocFactory componentDocFactory;

    private AggregateDocFactory aggregateDocFactory;
}
