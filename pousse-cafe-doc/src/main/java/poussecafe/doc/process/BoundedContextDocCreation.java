package poussecafe.doc.process;

import com.sun.javadoc.PackageDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocFactory;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocRepository;
import poussecafe.process.DomainProcess;

public class BoundedContextDocCreation extends DomainProcess {

    public void addBoundedContextDoc(PackageDoc packageDoc) {
        BoundedContextDoc boundedContextDoc = boundedContextDocFactory.newBoundedContextDoc(packageDoc);
        runInTransaction(BoundedContextDoc.class, () -> boundedContextDocRepository.add(boundedContextDoc));
    }

    private BoundedContextDocFactory boundedContextDocFactory;

    private BoundedContextDocRepository boundedContextDocRepository;
}
