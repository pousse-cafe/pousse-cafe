package poussecafe.doc.process;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import java.util.List;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDoc;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocFactory;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocKey;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocRepository;
import poussecafe.process.DomainProcess;

public class DomainProcessDocCreation extends DomainProcess {

    public void addDomainProcessDoc(BoundedContextDocKey boundedContextKey, ClassDoc classDoc) {
        DomainProcessDoc entityDoc = domainProcessDocFactory.newDomainProcessDoc(boundedContextKey, classDoc);
        runInTransaction(DomainProcessDoc.class, () -> domainProcessDocRepository.add(entityDoc));
    }

    private DomainProcessDocFactory domainProcessDocFactory;

    private DomainProcessDocRepository domainProcessDocRepository;

    public void addDomainProcessDocs(BoundedContextDocKey boundedContextKey, MethodDoc classDoc) {
        List<DomainProcessDoc> docs = domainProcessDocFactory.createDomainProcesses(boundedContextKey, classDoc);
        for(DomainProcessDoc doc : docs) {
            DomainProcessDocKey docKey = doc.attributes().key().value();
            BoundedContextComponentDoc boundedContextComponentDoc = doc.attributes().boundedContextComponentDoc().value();
            DomainProcessDoc existingDoc = domainProcessDocRepository.find(docKey);
            if(existingDoc != null) {
                if(boundedContextComponentDoc.componentDoc().hasDescription()) {
                    runInTransaction(DomainProcessDoc.class, () -> {
                        DomainProcessDoc toUpdate = domainProcessDocRepository.get(docKey);
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
