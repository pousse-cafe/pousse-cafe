package poussecafe.doc.process;

import com.sun.javadoc.ClassDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.doc.model.servicedoc.ServiceDoc;
import poussecafe.doc.model.servicedoc.ServiceDocFactory;
import poussecafe.doc.model.servicedoc.ServiceDocRepository;
import poussecafe.process.DomainProcess;

public class ServiceDocCreation extends DomainProcess {

    public void addServiceDoc(BoundedContextDocKey boundedContextKey, ClassDoc classDoc) {
        ServiceDoc serviceDoc = serviceDocFactory.newServiceDoc(boundedContextKey, classDoc);
        runInTransaction(ServiceDoc.class, () -> serviceDocRepository.add(serviceDoc));
    }

    private ServiceDocFactory serviceDocFactory;

    private ServiceDocRepository serviceDocRepository;
}
