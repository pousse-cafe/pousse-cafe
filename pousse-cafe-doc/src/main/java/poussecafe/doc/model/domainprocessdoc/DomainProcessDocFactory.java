package poussecafe.doc.model.domainprocessdoc;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import poussecafe.doc.AnnotationsResolver;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.ComponentDoc;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
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

        domainProcessDoc.steps(extractSteps(doc));

        return domainProcessDoc;
    }

    private ComponentDocFactory componentDocFactory;

    public static boolean isDomainProcessDoc(ClassDoc doc) {
        return ClassDocPredicates.documentsWithSuperclass(doc, DomainProcess.class);
    }

    public static String name(ClassDoc doc) {
        return doc.simpleTypeName();
    }

    private List<Step> extractSteps(ClassDoc doc) {
        HashMap<String, Step> steps = new HashMap<>();
        for(MethodDoc methodDoc : doc.methods()) {
            if(AnnotationsResolver.isStep(methodDoc)) {
                String stepName = AnnotationsResolver.step(methodDoc);
                String stepDescription = methodDoc.commentText();

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

                List<ToStep> toSteps = new ArrayList<>();
                toSteps.addAll(toDirectSteps(AnnotationsResolver.to(methodDoc)));
                toSteps.addAll(toDirectSteps(toExternals));

                toSteps.addAll(toEventualSteps(AnnotationsResolver.eventually(methodDoc)));
                steps.put(stepName, new Step.Builder()
                        .componentDoc(new ComponentDoc.Builder()
                                .name(stepName)
                                .description(stepDescription)
                                .build())
                        .tos(toSteps)
                        .consumedEvent(extractEvent(methodDoc))
                        .build());

                List<String> fromExternals = AnnotationsResolver.fromExternal(methodDoc);
                for(String fromExternal : fromExternals) {
                    Step fromExternalStep = steps.get(fromExternal);
                    ToStep additionalToStep = new ToStep.Builder()
                            .name(stepName)
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

    private Optional<String> extractEvent(MethodDoc methodDoc) {
        for(Parameter parameter : methodDoc.parameters()) {
            ClassDoc parameterClassDoc = parameter.type().asClassDoc();
            if(parameterClassDoc != null && isDomainEvent(parameterClassDoc)) {
                return Optional.of(parameterClassDoc.typeName());
            }
        }
        return Optional.empty();
    }

    private boolean isDomainEvent(ClassDoc parameterClassDoc) {
        return ClassDocPredicates.documentsWithSuperclass(parameterClassDoc, DomainEvent.class);
    }
}
