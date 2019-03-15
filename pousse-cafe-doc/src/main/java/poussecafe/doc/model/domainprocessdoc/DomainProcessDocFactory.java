package poussecafe.doc.model.domainprocessdoc;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import java.util.ArrayList;
import java.util.List;
import poussecafe.doc.AnnotationsResolver;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.ProcessDescription;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.ComponentDoc;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.domain.DomainException;
import poussecafe.domain.Factory;
import poussecafe.process.DomainProcess;

public class DomainProcessDocFactory extends Factory<DomainProcessDocKey, DomainProcessDoc, DomainProcessDoc.Attributes> {

    public DomainProcessDoc newDomainProcessDoc(BoundedContextDocKey boundedContextDocKey, ClassDoc doc) {
        if(!isDomainProcessDoc(doc)) {
            throw new DomainException("Class " + doc.name() + " is not a domain process");
        }

        String name = name(doc);
        DomainProcessDocKey key = new DomainProcessDocKey(doc.qualifiedName());
        DomainProcessDoc domainProcessDoc = newAggregateWithKey(key);
        domainProcessDoc.attributes().boundedContextComponentDoc().value(new BoundedContextComponentDoc.Builder()
                .boundedContextDocKey(boundedContextDocKey)
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

    public List<DomainProcessDoc> createDomainProcesses(BoundedContextDocKey boundedContextDocKey, MethodDoc methodDoc) {
        List<ProcessDescription> descriptions = AnnotationsResolver.processDescription(methodDoc);
        List<DomainProcessDoc> processes = new ArrayList<>();
        for(ProcessDescription description : descriptions) {
            DomainProcessDocKey key = new DomainProcessDocKey(methodDoc.qualifiedName() + "#" + description.name());
            DomainProcessDoc doc = newAggregateWithKey(key);
            doc.attributes().boundedContextComponentDoc().value(new BoundedContextComponentDoc.Builder()
                    .boundedContextDocKey(boundedContextDocKey)
                    .componentDoc(new ComponentDoc.Builder()
                            .name(description.name())
                            .description(description.description())
                            .build())
                    .build());
            processes.add(doc);
        }
        return processes;
    }

    public static boolean isDomainProcessDoc(MethodDoc doc) {
        return !AnnotationsResolver.processDescription(doc).isEmpty();
    }
}
