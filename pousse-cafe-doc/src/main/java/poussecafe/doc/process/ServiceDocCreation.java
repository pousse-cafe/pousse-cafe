package poussecafe.doc.process;

import javax.lang.model.element.TypeElement;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.doc.model.servicedoc.ServiceDoc;
import poussecafe.doc.model.servicedoc.ServiceDocFactory;
import poussecafe.doc.model.servicedoc.ServiceDocRepository;
import poussecafe.process.DomainProcess;

public class ServiceDocCreation extends DomainProcess {

    public void addServiceDoc(ModuleDocId moduleDocId, TypeElement classDoc) {
        ServiceDoc serviceDoc = serviceDocFactory.newServiceDoc(moduleDocId, classDoc);
        runInTransaction(ServiceDoc.class, () -> serviceDocRepository.add(serviceDoc));
    }

    private ServiceDocFactory serviceDocFactory;

    private ServiceDocRepository serviceDocRepository;
}
