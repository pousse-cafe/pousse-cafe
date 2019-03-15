package poussecafe.doc.model.domainprocessdoc;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import poussecafe.doc.AnnotationsResolver;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.doc.model.processstepdoc.StepMethodSignature;
import poussecafe.domain.DomainEvent;
import poussecafe.domain.DomainException;
import poussecafe.domain.Factory;
import poussecafe.process.DomainProcess;

public class DomainProcessDocFactory extends Factory<DomainProcessDocKey, DomainProcessDoc, DomainProcessDoc.Attributes> {

    public DomainProcessDoc newDomainProcessDoc(BoundedContextDocKey boundedContextDocKey, ClassDoc doc) {
        if(!isDomainProcessDoc(doc)) {
            throw new DomainException("Class " + doc.name() + " is not a domain process");
        }

        String name = name(doc);
        DomainProcessDocKey key = DomainProcessDocKey.ofClassName(doc.qualifiedName());
        DomainProcessDoc domainProcessDoc = newAggregateWithKey(key);
        domainProcessDoc.attributes().boundedContextComponentDoc().value(new BoundedContextComponentDoc.Builder()
                .boundedContextDocKey(boundedContextDocKey)
                .componentDoc(componentDocFactory.buildDoc(name, doc))
                .build());

        List<StepMethodSignature> steps = extractSteps(doc);
        domainProcessDoc.attributes().steps().value(steps);
        domainProcessDoc.attributes().fromExternals().value(fromExternals(doc));
        domainProcessDoc.attributes().toExternals().value(toExternals(doc));

        return domainProcessDoc;
    }

    private ComponentDocFactory componentDocFactory;

    public static boolean isDomainProcessDoc(ClassDoc doc) {
        return ClassDocPredicates.documentsWithSuperclass(doc, DomainProcess.class);
    }

    public static String name(ClassDoc doc) {
        return doc.simpleTypeName();
    }

    private List<StepMethodSignature> extractSteps(ClassDoc doc) {
        List<StepMethodSignature> steps = new ArrayList<>();
        for(MethodDoc methodDoc : doc.methods()) {
            if(AnnotationsResolver.isStep(methodDoc)) {
                StepMethodSignature signature = stepMethodSignature(methodDoc);
                steps.add(signature);
            }
        }
        return steps;
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

    private Optional<String> consumedEvent(MethodDoc methodDoc) {
        if(methodDoc.parameters().length > 0 &&
                methodDoc.parameters()[0].type().asClassDoc() != null &&
                ClassDocPredicates.documentsWithSuperinterface(methodDoc.parameters()[0].type().asClassDoc(), DomainEvent.class)) {
            return Optional.of(methodDoc.parameters()[0].type().asClassDoc().name());
        } else {
            return Optional.empty();
        }
    }

    private Map<StepName, List<StepName>> fromExternals(ClassDoc doc) {
        return externals(doc, AnnotationsResolver::fromExternal);
    }

    private Map<StepName, List<StepName>> externals(ClassDoc doc, Function<MethodDoc, List<String>> externalsSupplier) {
        Map<StepName, List<StepName>> steps = new HashMap<>();
        for(MethodDoc methodDoc : doc.methods()) {
            if(AnnotationsResolver.isStep(methodDoc)) {
                List<String> externals = externalsSupplier.apply(methodDoc);
                if(!externals.isEmpty()) {
                    StepMethodSignature signature = stepMethodSignature(methodDoc);
                    StepName stepName = new StepName(signature);
                    List<StepName> tos;
                    if(steps.containsKey(stepName)) {
                        tos = steps.get(stepName);
                    } else {
                        tos = new ArrayList<>();
                        steps.put(stepName, tos);
                    }
                    for(String to : externals) {
                        tos.add(new StepName(to));
                    }
                }
            }
        }
        return steps;
    }

    private Map<StepName, List<StepName>> toExternals(ClassDoc doc) {
        return externals(doc, AnnotationsResolver::toExternal);
    }
}
