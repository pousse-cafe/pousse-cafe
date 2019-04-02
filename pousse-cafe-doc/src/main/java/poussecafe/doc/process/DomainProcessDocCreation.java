package poussecafe.doc.process;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import java.util.List;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDoc;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocFactory;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocId;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocRepository;
import poussecafe.process.DomainProcess;

public class DomainProcessDocCreation extends DomainProcess {

    public void addDomainProcessDoc(BoundedContextDocId boundedContextId, ClassDoc classDoc) {
        DomainProcessDoc entityDoc = domainProcessDocFactory.newDomainProcessDoc(boundedContextId, classDoc);
        runInTransaction(DomainProcessDoc.class, () -> domainProcessDocRepository.add(entityDoc));
    }

    private DomainProcessDocFactory domainProcessDocFactory;

    private DomainProcessDocRepository domainProcessDocRepository;

    public void addDomainProcessDocs(BoundedContextDocId boundedContextId, MethodDoc classDoc) {
        List<DomainProcessDoc> docs = domainProcessDocFactory.createDomainProcesses(boundedContextId, classDoc);
        for(DomainProcessDoc doc : docs) {
            DomainProcessDocId docId = doc.attributes().identifier().value();
            BoundedContextComponentDoc boundedContextComponentDoc = doc.attributes().boundedContextComponentDoc().value();
            DomainProcessDoc existingDoc = domainProcessDocRepository.find(docId);
            if(existingDoc != null) {
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
