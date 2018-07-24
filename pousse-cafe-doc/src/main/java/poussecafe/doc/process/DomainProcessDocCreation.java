package poussecafe.doc.process;

import com.sun.javadoc.ClassDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDoc;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocFactory;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDocRepository;
import poussecafe.process.DomainProcess;

public class DomainProcessDocCreation extends DomainProcess {

    public void addDomainProcessDoc(BoundedContextDocKey boundedContextKey, ClassDoc classDoc) {
        DomainProcessDoc entityDoc = valueObjectDocFactory.newDomainProcessDoc(boundedContextKey, classDoc);
        runInTransaction(DomainProcessDoc.class, () -> valueObjectDocRepository.add(entityDoc));
    }

    private DomainProcessDocFactory valueObjectDocFactory;

    private DomainProcessDocRepository valueObjectDocRepository;
}
