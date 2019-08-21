package poussecafe.doc.model.servicedoc;

import javax.lang.model.element.TypeElement;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.model.ModuleComponentDoc;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.domain.DomainException;
import poussecafe.domain.Factory;
import poussecafe.domain.Service;

public class ServiceDocFactory extends Factory<ServiceDocId, ServiceDoc, ServiceDoc.Attributes> {

    public ServiceDoc newServiceDoc(ModuleDocId moduleDocId, TypeElement classDoc) {
        if(!isServiceDoc(classDoc)) {
            throw new DomainException("Class " + classDoc.getQualifiedName() + " is not a service");
        }

        String name = classDoc.getSimpleName().toString();
        ServiceDocId id = ServiceDocId.ofClassName(classDoc.getQualifiedName().toString());
        ServiceDoc serviceDoc = newAggregateWithId(id);
        serviceDoc.attributes().moduleComponentDoc().value(new ModuleComponentDoc.Builder()
                .moduleDocId(moduleDocId)
                .componentDoc(componentDocFactory.buildDoc(name, classDoc))
                .build());
        return serviceDoc;
    }

    private ComponentDocFactory componentDocFactory;

    public boolean isServiceDoc(TypeElement classDoc) {
        return classDocPredicates.documentsWithSuperinterface(classDoc, Service.class);
    }

    private ClassDocPredicates classDocPredicates;
}
