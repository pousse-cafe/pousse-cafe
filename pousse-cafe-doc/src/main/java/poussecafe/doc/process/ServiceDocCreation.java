package poussecafe.doc.process;

import com.sun.javadoc.ClassDoc;
import poussecafe.doc.model.servicedoc.ServiceDoc;
import poussecafe.doc.model.servicedoc.ServiceDocFactory;
import poussecafe.doc.model.servicedoc.ServiceDocRepository;
import poussecafe.process.DomainProcess;

public class ServiceDocCreation extends DomainProcess {

    public void addServiceDoc(ClassDoc classDoc) {
        ServiceDoc serviceDoc = serviceDocFactory.newServiceDoc(classDoc);
        runInTransaction(ServiceDoc.class, () -> serviceDocRepository.add(serviceDoc));
    }

    private ServiceDocFactory serviceDocFactory;

    private ServiceDocRepository serviceDocRepository;
}
