package poussecafe.doc.model.domainprocessdoc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.ProcessDescription;
import poussecafe.doc.model.AnnotationsResolver;
import poussecafe.doc.model.ComponentDoc;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.doc.model.ModuleComponentDoc;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.domain.DomainException;
import poussecafe.domain.Factory;
import poussecafe.domain.Process;
import poussecafe.process.DomainProcess;

public class DomainProcessDocFactory extends Factory<DomainProcessDocId, DomainProcessDoc, DomainProcessDoc.Attributes> {

    public DomainProcessDoc newDomainProcessDoc(ModuleDocId moduleDocId, TypeElement doc) {
        if(!isDomainProcessDoc(doc)) {
            throw new DomainException("Class " + doc.getQualifiedName() + " is not a domain process");
        }

        String name = name(doc);
        DomainProcessDocId id = new DomainProcessDocId(doc.getQualifiedName().toString());
        DomainProcessDoc domainProcessDoc = newAggregateWithId(id);
        domainProcessDoc.attributes().moduleComponentDoc().value(new ModuleComponentDoc.Builder()
                .moduleDocId(moduleDocId)
                .componentDoc(componentDocFactory.buildDoc(name, doc))
                .build());

        return domainProcessDoc;
    }

    public boolean isDomainProcessDoc(TypeElement doc) {
        return classDocPredicates.documentsWithSuperclass(doc, DomainProcess.class)
                || classDocPredicates.documentsWithSuperinterface(doc, Process.class);
    }

    private ClassDocPredicates classDocPredicates;

    public String name(TypeElement doc) {
        return doc.getSimpleName().toString();
    }

    private ComponentDocFactory componentDocFactory;

    public List<DomainProcessDoc> createDomainProcesses(ModuleDocId moduleDocId, ExecutableElement methodDoc) {
        if(!isDomainProcessDoc(methodDoc)) {
            throw new DomainException("Method " + methodDoc.getSimpleName() + " does not define any domain process");
        }

        List<ProcessDescription> descriptions = annotationsResolver.processDescription(methodDoc);
        Set<String> detectedDomainProcesses = new HashSet<>();
        List<DomainProcessDoc> processes = new ArrayList<>();
        for(ProcessDescription description : descriptions) {
            detectedDomainProcesses.add(description.name());
            DomainProcessDoc doc = buildDomainProcessDoc(moduleDocId, description);
            processes.add(doc);
        }
        List<String> names = annotationsResolver.process(methodDoc);
        for(String name : names) {
            if(!detectedDomainProcesses.contains(name)) {
                detectedDomainProcesses.add(name);
                DomainProcessDoc doc = buildDomainProcessDoc(moduleDocId, new ProcessDescription.Builder()
                        .name(name)
                        .description("")
                        .build());
                processes.add(doc);
            }
        }
        return processes;
    }

    private AnnotationsResolver annotationsResolver;

    private DomainProcessDoc buildDomainProcessDoc(ModuleDocId moduleDocId,
            ProcessDescription description) {
        DomainProcessDocId id = new DomainProcessDocId(moduleDocId.stringValue() + "." + description.name());
        DomainProcessDoc doc = newAggregateWithId(id);
        doc.attributes().moduleComponentDoc().value(new ModuleComponentDoc.Builder()
                .moduleDocId(moduleDocId)
                .componentDoc(new ComponentDoc.Builder()
                        .name(description.name())
                        .description(description.description())
                        .build())
                .build());
        return doc;
    }

    public boolean isDomainProcessDoc(ExecutableElement doc) {
        return !(annotationsResolver.processDescription(doc).isEmpty() && annotationsResolver.process(doc).isEmpty());
    }
}
