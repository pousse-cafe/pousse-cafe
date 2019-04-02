package poussecafe.doc.model.domainprocessdoc;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import poussecafe.doc.AnnotationsResolver;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.ProcessDescription;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.ComponentDoc;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.domain.DomainException;
import poussecafe.domain.Factory;
import poussecafe.process.DomainProcess;

public class DomainProcessDocFactory extends Factory<DomainProcessDocId, DomainProcessDoc, DomainProcessDoc.Attributes> {

    public DomainProcessDoc newDomainProcessDoc(BoundedContextDocId boundedContextDocId, ClassDoc doc) {
        if(!isDomainProcessDoc(doc)) {
            throw new DomainException("Class " + doc.name() + " is not a domain process");
        }

        String name = name(doc);
        DomainProcessDocId id = new DomainProcessDocId(doc.qualifiedName());
        DomainProcessDoc domainProcessDoc = newAggregateWithId(id);
        domainProcessDoc.attributes().boundedContextComponentDoc().value(new BoundedContextComponentDoc.Builder()
                .boundedContextDocId(boundedContextDocId)
                .componentDoc(componentDocFactory.buildDoc(name, doc))
                .build());

        return domainProcessDoc;
    }

    public static boolean isDomainProcessDoc(ClassDoc doc) {
        return ClassDocPredicates.documentsWithSuperclass(doc, DomainProcess.class);
    }

    public static String name(ClassDoc doc) {
        return doc.simpleTypeName();
    }

    private ComponentDocFactory componentDocFactory;

    public List<DomainProcessDoc> createDomainProcesses(BoundedContextDocId boundedContextDocId, MethodDoc methodDoc) {
        if(!isDomainProcessDoc(methodDoc)) {
            throw new DomainException("Method " + methodDoc.name() + " does not define any domain process");
        }

        List<ProcessDescription> descriptions = AnnotationsResolver.processDescription(methodDoc);
        Set<String> detectedDomainProcesses = new HashSet<>();
        List<DomainProcessDoc> processes = new ArrayList<>();
        for(ProcessDescription description : descriptions) {
            detectedDomainProcesses.add(description.name());
            DomainProcessDoc doc = buildDomainProcessDoc(boundedContextDocId, description);
            processes.add(doc);
        }
        List<String> names = AnnotationsResolver.process(methodDoc);
        for(String name : names) {
            if(!detectedDomainProcesses.contains(name)) {
                detectedDomainProcesses.add(name);
                DomainProcessDoc doc = buildDomainProcessDoc(boundedContextDocId, new ProcessDescription.Builder()
                        .name(name)
                        .description("")
                        .build());
                processes.add(doc);
            }
        }
        return processes;
    }

    private DomainProcessDoc buildDomainProcessDoc(BoundedContextDocId boundedContextDocId,
            ProcessDescription description) {
        DomainProcessDocId id = new DomainProcessDocId(boundedContextDocId.getValue() + "." + description.name());
        DomainProcessDoc doc = newAggregateWithId(id);
        doc.attributes().boundedContextComponentDoc().value(new BoundedContextComponentDoc.Builder()
                .boundedContextDocId(boundedContextDocId)
                .componentDoc(new ComponentDoc.Builder()
                        .name(description.name())
                        .description(description.description())
                        .build())
                .build());
        return doc;
    }

    public static boolean isDomainProcessDoc(MethodDoc doc) {
        return !(AnnotationsResolver.processDescription(doc).isEmpty() && AnnotationsResolver.process(doc).isEmpty());
    }
}
