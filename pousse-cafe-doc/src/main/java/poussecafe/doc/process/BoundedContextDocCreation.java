package poussecafe.doc.process;

import javax.lang.model.element.PackageElement;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocFactory;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocRepository;
import poussecafe.process.DomainProcess;

public class BoundedContextDocCreation extends DomainProcess {

    public void addBoundedContextDoc(PackageElement classDoc) {
        BoundedContextDoc boundedContextDoc = boundedContextDocFactory.newBoundedContextDoc(classDoc);
        runInTransaction(BoundedContextDoc.class, () -> boundedContextDocRepository.add(boundedContextDoc));
    }

    private BoundedContextDocFactory boundedContextDocFactory;

    private BoundedContextDocRepository boundedContextDocRepository;
}
