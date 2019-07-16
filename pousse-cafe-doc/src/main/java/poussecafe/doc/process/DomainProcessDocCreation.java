package poussecafe.doc.process;

import java.util.List;
import java.util.Optional;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDoc;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocFactory;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocId;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocRepository;
import poussecafe.process.DomainProcess;

public class DomainProcessDocCreation extends DomainProcess {

    public void addDomainProcessDoc(BoundedContextDocId boundedContextId, TypeElement classDoc) {
        DomainProcessDoc entityDoc = domainProcessDocFactory.newDomainProcessDoc(boundedContextId, classDoc);
        runInTransaction(DomainProcessDoc.class, () -> domainProcessDocRepository.add(entityDoc));
    }

    private DomainProcessDocFactory domainProcessDocFactory;

    private DomainProcessDocRepository domainProcessDocRepository;

    public void addDomainProcessDocs(BoundedContextDocId boundedContextId, ExecutableElement classDoc) {
        List<DomainProcessDoc> docs = domainProcessDocFactory.createDomainProcesses(boundedContextId, classDoc);
        for(DomainProcessDoc doc : docs) {
            DomainProcessDocId docId = doc.attributes().identifier().value();
            BoundedContextComponentDoc boundedContextComponentDoc = doc.attributes().boundedContextComponentDoc().value();
            Optional<DomainProcessDoc> existingDoc = domainProcessDocRepository.getOptional(docId);
            if(existingDoc.isPresent()) {
                if(boundedContextComponentDoc.componentDoc().hasDescription()) {
                    runInTransaction(DomainProcessDoc.class, () -> {
                        DomainProcessDoc toUpdate = domainProcessDocRepository.get(docId);
                        toUpdate.attributes().boundedContextComponentDoc().value(boundedContextComponentDoc);
                        domainProcessDocRepository.update(toUpdate);
                    });
                }
            } else {
                runInTransaction(DomainProcessDoc.class, () -> domainProcessDocRepository.add(doc));
            }
        }
    }
}
