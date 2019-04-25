package poussecafe.doc.process;

import javax.lang.model.element.TypeElement;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.doc.model.servicedoc.ServiceDoc;
import poussecafe.doc.model.servicedoc.ServiceDocFactory;
import poussecafe.doc.model.servicedoc.ServiceDocRepository;
import poussecafe.process.DomainProcess;

public class ServiceDocCreation extends DomainProcess {

    public void addServiceDoc(BoundedContextDocId boundedContextId, TypeElement classDoc) {
        ServiceDoc serviceDoc = serviceDocFactory.newServiceDoc(boundedContextId, classDoc);
        runInTransaction(ServiceDoc.class, () -> serviceDocRepository.add(serviceDoc));
    }

    private ServiceDocFactory serviceDocFactory;

    private ServiceDocRepository serviceDocRepository;
}
