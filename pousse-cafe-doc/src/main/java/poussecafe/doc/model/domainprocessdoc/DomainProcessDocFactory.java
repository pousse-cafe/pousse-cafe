package poussecafe.doc.model.domainprocessdoc;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import poussecafe.doc.AnnotationsResolver;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.ComponentDoc;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.doc.model.aggregatedoc.AggregateDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocRepository;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.doc.model.factorydoc.FactoryDoc;
import poussecafe.doc.model.factorydoc.FactoryDocRepository;
import poussecafe.doc.model.step.StepDoc;
import poussecafe.doc.model.step.StepMethodSignature;
import poussecafe.domain.DomainEvent;
import poussecafe.domain.DomainException;
import poussecafe.domain.Factory;
import poussecafe.process.DomainProcess;

import static java.util.stream.Collectors.toList;

public class DomainProcessDocFactory extends Factory<DomainProcessDocKey, DomainProcessDoc, DomainProcessDoc.Data> {

    public DomainProcessDoc newDomainProcessDoc(BoundedContextDocKey boundedContextDocKey, ClassDoc doc) {
        if(!isDomainProcessDoc(doc)) {
            throw new DomainException("Class " + doc.name() + " is not a domain process");
        }

        String name = name(doc);
        DomainProcessDocKey key = DomainProcessDocKey.ofClassName(doc.qualifiedName());
        DomainProcessDoc domainProcessDoc = newStorableWithKey(key);
        domainProcessDoc.boundedContextComponentDoc(new BoundedContextComponentDoc.Builder()
                .boundedContextDocKey(boundedContextDocKey)
                .componentDoc(componentDocFactory.buildDoc(name, doc))
                .build());

        domainProcessDoc.steps(extractSteps(boundedContextDocKey, doc));

        return domainProcessDoc;
    }

    private ComponentDocFactory componentDocFactory;

    public static boolean isDomainProcessDoc(ClassDoc doc) {
        return ClassDocPredicates.documentsWithSuperclass(doc, DomainProcess.class);
    }

    public static String name(ClassDoc doc) {
        return doc.simpleTypeName();
    }

    private List<Step> extractSteps(BoundedContextDocKey boundedContextDocKey, ClassDoc doc) {
        HashMap<String, Step> steps = new HashMap<>();

        HashMap<String, List<StepMethodSignature>> eventToStep = new HashMap<>();
        for(MethodDoc methodDoc : doc.methods()) {
            if(AnnotationsResolver.isStep(methodDoc)) {
                StepMethodSignature stepMethodSignature = stepMethodSignature(methodDoc);
                if(stepMethodSignature.consumedEventName().isPresent()) {
                    String consumedEventName = stepMethodSignature.consumedEventName().get();
                    List<StepMethodSignature> signatures = eventToStep.get(consumedEventName);
                    if(signatures == null) {
                        signatures = new ArrayList<>();
                        eventToStep.put(consumedEventName, signatures);
                    }
                    signatures.add(stepMethodSignature);
                }
            }
        }

        for(MethodDoc methodDoc : doc.methods()) {
            if(AnnotationsResolver.isStep(methodDoc)) {
                StepDoc stepDoc = locateStepDoc(boundedContextDocKey, methodDoc);

                List<ToStep> toSteps = new ArrayList<>();
                List<String> tos = locateTos(methodDoc, stepDoc, eventToStep);
                toSteps.addAll(toDirectSteps(tos));

                List<String> toExternals = AnnotationsResolver.toExternal(methodDoc);
                for(String toExternal : toExternals) {
                    Step toExternalStep = steps.get(toExternal);
                    if(toExternalStep == null) {
                        toExternalStep = new Step.Builder()
                                .componentDoc(new ComponentDoc.Builder()
                                        .name(toExternal)
                                        .description("")
                                        .build())
                                .external(true)
                                .build();
                        steps.put(toExternal, toExternalStep);
                    }
                }

                toSteps.addAll(toDirectSteps(toExternals));

                toSteps.addAll(toEventualSteps(AnnotationsResolver.eventually(methodDoc)));
                steps.put(stepDoc.componentDoc().name(), new Step.Builder()
                        .componentDoc(stepDoc.componentDoc())
                        .tos(toSteps)
                        .build());

                List<String> fromExternals = AnnotationsResolver.fromExternal(methodDoc);
                for(String fromExternal : fromExternals) {
                    Step fromExternalStep = steps.get(fromExternal);
                    ToStep additionalToStep = new ToStep.Builder()
                            .name(stepDoc.componentDoc().name())
                            .directly(true)
                            .build();
                    if(fromExternalStep == null) {
                        fromExternalStep = new Step.Builder()
                                .componentDoc(new ComponentDoc.Builder()
                                        .name(fromExternal)
                                        .description("")
                                        .build())
                                .external(true)
                                .to(additionalToStep)
                                .build();
                    } else {
                        fromExternalStep = new Step.Builder()
                                .step(fromExternalStep)
                                .to(additionalToStep)
                                .build();
                    }
                    steps.put(fromExternalStep.componentDoc().name(), fromExternalStep);
                }
            }
        }
        return steps.values().stream().collect(toList());
    }

    private StepMethodSignature stepMethodSignature(MethodDoc methodDoc) {
        List<String> stepNames = AnnotationsResolver.step(methodDoc);
        if(stepNames.size() != 1) {
            throw new DomainException("Domain processes listeners must be tagged with a single step");
        }
        Optional<String> consumedEvent = consumedEvent(methodDoc);
        ComponentMethodName componentMethodName = ComponentMethodName.parse(stepNames.get(0));
        return new StepMethodSignature.Builder()
                .componentMethodName(componentMethodName)
                .consumedEventName(consumedEvent)
                .build();
    }

    private List<String> locateTos(MethodDoc methodDoc, StepDoc stepDoc,
            HashMap<String, List<StepMethodSignature>> eventToStep) {
        List<String> tos = new ArrayList<>();
        for(String producedEvent : stepDoc.producedEvents()) {
            List<StepMethodSignature> signatures = eventToStep.get(producedEvent);
            if(signatures != null) {
                tos.addAll(signatures.stream().map(StepMethodSignature::toString).collect(toList()));
            }
        }
        return tos;
    }

    private Optional<String> consumedEvent(MethodDoc methodDoc) {
        if(methodDoc.parameters().length > 0 &&
                methodDoc.parameters()[0].type().asClassDoc() != null &&
                ClassDocPredicates.documentsWithSuperclass(methodDoc.parameters()[0].type().asClassDoc(), DomainEvent.class)) {
            return Optional.of(methodDoc.parameters()[0].type().asClassDoc().name());
        } else {
            return Optional.empty();
        }
    }

    private StepDoc locateStepDoc(BoundedContextDocKey boundedContextDocKey,
            MethodDoc methodDoc) {
        StepMethodSignature stepMethodSignature = stepMethodSignature(methodDoc);
        AggregateDoc aggregateDoc = aggregateDocRepository
                .findByBoundedContextKeyAndName(boundedContextDocKey, stepMethodSignature.componentMethodName().componentName());
        if(aggregateDoc != null) {
            return aggregateDoc
                    .stepDocBySignature(stepMethodSignature)
                    .orElseThrow(() -> new DomainException("Method not found " + stepMethodSignature));
        } else {
            FactoryDoc factoryDoc = factoryDocRepository
                    .findByBoundedContextKeyAndName(boundedContextDocKey, stepMethodSignature.componentMethodName().componentName());
            if(factoryDoc != null) {
                return factoryDoc
                        .stepDocBySignature(stepMethodSignature)
                        .orElseThrow(() -> new DomainException("Method not found " + stepMethodSignature));
            } else {
                throw new DomainException("Method not found " + stepMethodSignature);
            }
        }
    }

    private FactoryDocRepository factoryDocRepository;

    private AggregateDocRepository aggregateDocRepository;

    private List<ToStep> toDirectSteps(List<String> tos) {
        List<ToStep> toSteps = new ArrayList<>();
        for(String to : tos) {
            toSteps.add(new ToStep.Builder()
                    .name(to)
                    .directly(true)
                    .build());
        }
        return toSteps;
    }

    private List<ToStep> toEventualSteps(List<String> tos) {
        List<ToStep> toSteps = new ArrayList<>();
        for(String to : tos) {
            toSteps.add(new ToStep.Builder()
                    .name(to)
                    .directly(false)
                    .build());
        }
        return toSteps;
    }
}
